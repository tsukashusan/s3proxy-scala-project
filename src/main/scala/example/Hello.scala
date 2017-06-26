package example

import java.util.Properties
import java.net.URI
import java.nio.file.Files
import java.nio.file.FileSystems
import java.nio.file.StandardOpenOption

import org.eclipse.jetty.util.component.AbstractLifeCycle
import org.jclouds.ContextBuilder
import org.jclouds.blobstore.BlobStoreContext
import org.jclouds.Constants

import org.gaul.s3proxy.S3Proxy

object Hello extends Greeting with App {
  println("hello")
  s3ProxyStart
  def s3ProxyStart() : Unit = {
    val path = FileSystems.getDefault().getPath("conf", "s3proxy.conf")
    val inputStream = Files.newInputStream(path, StandardOpenOption.READ)
    val properties = new Properties
    properties.load(inputStream)
    Option(properties.getProperty(Constants.PROPERTY_PROVIDER)) match {
      case Some(provider) => Option(properties.getProperty(Constants.PROPERTY_IDENTITY)) match {
        case Some(identity) => Option(properties.getProperty(Constants.PROPERTY_CREDENTIAL)) match {
          case Some(credential) => {
            val context = ContextBuilder.newBuilder(provider).
                          credentials(identity, credential).
                          overrides(properties).build(classOf[BlobStoreContext])
            val s3Proxy = S3Proxy.builder().blobStore(context.getBlobStore()).
                          endpoint(URI.create("http://127.0.0.1:8080")).build()
            s3Proxy.start()
            while (!s3Proxy.getState().equals(AbstractLifeCycle.STARTED)) {
               Thread.sleep(1)
               println("Not Start!")
            }
            println("Start!")
          }
          case None => println("$Constants.PROPERTY_CREDENTIAL is not exists.")
        }
        case None => println("Constants.PROPERTY_IDENTITY is not exists.")
      }
      case None => println("$Constants.PROPERTY_PROVIDER is not exists")
    }
  }
}

trait Greeting {
  lazy val greeting: String = "hello"
}

package ch.ahoegger.docbox.shared.helloworld;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.helloworld.HelloWorldFormData;

/**
 * <h3>{@link IHelloWorldFormService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IHelloWorldFormService extends IService {
	HelloWorldFormData load(HelloWorldFormData input);
}

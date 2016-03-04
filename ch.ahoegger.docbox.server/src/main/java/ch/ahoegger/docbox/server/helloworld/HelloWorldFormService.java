package ch.ahoegger.docbox.server.helloworld;

import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.shared.helloworld.HelloWorldFormData;
import ch.ahoegger.docbox.shared.helloworld.IHelloWorldFormService;

/**
 * <h3>{@link HelloWorldFormService}</h3>
 *
 * @author aho
 */
public class HelloWorldFormService implements IHelloWorldFormService {

	@Override
	public HelloWorldFormData load(HelloWorldFormData input) {
		StringBuilder msg = new StringBuilder();
		msg.append("Hello ").append(ServerSession.get().getUserId()).append("!");
		input.getMessage().setValue(msg.toString());
		return input;
	}
}

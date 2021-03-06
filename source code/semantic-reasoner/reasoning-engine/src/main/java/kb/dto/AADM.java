package kb.dto;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.rdf4j.model.IRI;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kb.KBApi;
import kb.utils.MyUtils;

public class AADM extends Resource {

	IRI user;
	ZonedDateTime createdAt;
	String version;

	Set<NodeFull> templates;

	public AADM(IRI iri) {
		super(iri);
		templates = new HashSet<NodeFull>();
	}

	public void build(KBApi api) throws IOException {
		for (NodeFull nodeFull : templates) {
			nodeFull.build(api);
		}
	}

	public IRI getUser() {
		return user;
	}

	public void setUser(IRI user) {
		this.user = user;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(ZonedDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setTemplates(Set<NodeFull> templates) {
		this.templates = templates;
	}

	public void addTemplate(NodeFull n) {
		templates.add(n);
	}

	@Override
	public JsonElement serialise() throws IOException {
		JsonObject aadm = new JsonObject();

		JsonObject data = new JsonObject();
		data.addProperty("type", "AbstractApplicationDeploymentModel");
		data.addProperty("createdBy", user.toString());
		data.addProperty("createdAt", createdAt.toString());
		data.addProperty("version", "1");

		JsonArray participants = new JsonArray();
		for (NodeFull nodeFull : templates) {
			participants.add(nodeFull.uri);
		}
		data.add("participants", participants);
		aadm.add(uri, data);

		// recursion

		LinkedList<NodeFull> list = new LinkedList<NodeFull>(templates);
		HashSet<String> bag = new HashSet<String>();

		NodeFull nodeFull;
		while (!list.isEmpty()) {
			nodeFull = list.pop();
			if (nodeFull == null || bag.contains(nodeFull.uri))
				continue;
			bag.add(nodeFull.uri);
			JsonElement serialise = nodeFull.serialise();
			aadm.add(nodeFull.uri, serialise);

//			System.out.println("---->" + nodeFull.relevantUris);
			list.addAll(getNodes(nodeFull.relevantUris));
			System.out.println("list: " + list.size());
			System.out.println("bag: " + bag.size());
		}

		System.out.println("Bag:" + MyUtils.getGson(true).toJson(bag));

		return aadm;
	}

	private Set<NodeFull> getNodes(Set<String> uris) throws IOException {
		KBApi kb = new KBApi();
		Set<NodeFull> results = new HashSet<NodeFull>();

		for (String u : uris) {
			results.add(kb.getNode(u));
		}

		kb.shutDown();
//		System.out.println("---->" + results.size());
		return results;

	}

	@Override
	public JsonElement serialiseCompact() {
		// TODO Auto-generated method stub
		return null;
	}

}

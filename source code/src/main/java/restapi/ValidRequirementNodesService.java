package restapi;

import java.io.IOException;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kb.KBApi;
import kb.dto.Node;

@Path("/valid-requirement-nodes")
@Api()
public class ValidRequirementNodesService extends AbstractService {

	@GET
	@Produces("application/json")
	@ApiOperation(
			value = "Returns nodes that satisfy a certain requirement",
//			response = String.class,
			responseContainer = "List")
	public Response getValidNodes(
			@ApiParam(
					value = "the name of the requirement, e.g. host",
					required = true,
					defaultValue = "host") @QueryParam("requirement") String requirement,
			@ApiParam(
					value = "the node type for which the requirement is relevant, e.g. tosca.nodes.SoftwareComponent",
					required = true,
					defaultValue = "tosca.nodes.SoftwareComponent") @QueryParam("nodeType") String nodeType)
			throws IOException {

		KBApi api = new KBApi();
		Set<Node> nodes = api.getRequirementValidNodes(requirement, nodeType);
		api.shutDown();

		JsonObject _nodes = new JsonObject();
		JsonArray array = new JsonArray();
		for (Node node : nodes) {
			array.add(node.serialise());
		}
		_nodes.add("data", array);

		return Response.ok(_nodes.toString()).build();
	}

}

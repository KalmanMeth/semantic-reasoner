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
import kb.dto.Interface;

@Path("/interfaces")
@Api()
public class InterfaceService extends AbstractService {

	@GET
	@Produces("application/json")
	@ApiOperation(
			value = "Returns the interfaces of a single TOSCA resource",
//			response = String.class,
			responseContainer = "List")
	public Response getInterface(
			@ApiParam(
					value = "A TOSCA resource, e.g. a node",
					required = true,
					defaultValue = "my.nodes.SkylineExtractor") @QueryParam("resource") String resource)
			throws IOException {

		KBApi api = new KBApi();
		Set<Interface> interfaces = api.getInterfaces(resource, false);
		api.shutDown();

		JsonObject _interfaces = new JsonObject();
		JsonArray array = new JsonArray();
		for (Interface _interface : interfaces) {
			array.add(_interface.serialise());
		}
		_interfaces.add("data", array);

		return Response.ok(_interfaces.toString()).build();
	}

}

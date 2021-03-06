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
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import kb.KBApi;
import kb.dto.Attribute;

@SwaggerDefinition(
		info = @Info(
				description = "REST API for the SODALITE Reasoner",
				version = "0.6",
				title = "SODALITE REST API",
				contact = @Contact(name = "George Meditskos", email = "gmeditsk@iti.gr")),
		produces = {
				"application/json"
		},
		schemes = { SwaggerDefinition.Scheme.HTTP },
		basePath = "reasoner-api/v0.6",
		host = "http://160.40.52.200:8084")

@Path("/attributes")
@Api()
public class AttributeService extends AbstractService {

	@GET
	@Produces("application/json")
	@ApiOperation(
			value = "Returns the attributes of a single TOSCA resource",
//			response = Attribute.class,
			responseContainer = "List")
	public Response getAttributeProperty(
			@ApiParam(
					value = "A TOSCA resource, e.g. a node",
					required = true,
					defaultValue = "tosca.nodes.Compute") @QueryParam("resource") String resource)
			throws IOException {

		KBApi api = new KBApi();
		Set<Attribute> attributes = api.getAttributes(resource, false);
		api.shutDown();

		JsonObject _attributes = new JsonObject();
		JsonArray array = new JsonArray();
		for (Attribute attribute : attributes) {
			array.add(attribute.serialise());
		}
		_attributes.add("data", array);

		return Response.ok(_attributes.toString()).build();
	}

}

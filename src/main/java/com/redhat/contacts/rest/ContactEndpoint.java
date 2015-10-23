package com.redhat.contacts.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;

import com.redhat.contacts.model.Contact;
import com.redhat.contacts.service.ContactService;

@Path("/contacts")
public class ContactEndpoint {

	@Inject
	ContactService service;

	@POST
	@Consumes("application/json")
	public Response create(Contact entity) throws Exception {

		service.create(entity);
		return Response.created(
				UriBuilder.fromResource(ContactEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Contact entity = service.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		service.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		Contact entity = service.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Produces("application/json")
	public List<Contact> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		return service.listAll(startPosition, maxResult);

	}

	@Path("/searchCSV")
	@GET()
	@Produces(MediaType.TEXT_PLAIN)
	public/* List<Contact> */Response searchCSV(@QueryParam("q") String search,
			@QueryParam("t") @DefaultValue("") String type,
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) throws Exception {
		List<Contact> ret = null;
		String r = "";

		switch (type) {
		case "f":
			ret = service.searchQuery(search);
		case "w":
			ret = service.searchWildcard(search);
		case "l":
			ret = service.searchLucene(search);

		default:
			ret = service.searchLucene(search);
		}

		for (Contact contact : ret) {
			r += StringUtils.join(new String[] { contact.getId().toString(),
					contact.getNom(), contact.getPrenom(), contact.getVille(),
					contact.getEmail() }, '\t')
					+ "\n";
		}

		return Response.status(Status.ACCEPTED).type(MediaType.TEXT_PLAIN)
				.entity(r).build();
	}

	@Path("/search")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public List<Contact> search(@QueryParam("q") String search,
			@QueryParam("t") @DefaultValue("") String type,
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) throws Exception {
		List<Contact> ret = null;

		switch (type) {
		case "f":
			ret = service.searchQuery(search);
		case "w":
			ret = service.searchWildcard(search);
		case "l":
			ret = service.searchLucene(search);

		default:
			ret = service.searchLucene(search);
		}

		return ret;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, Contact entity) {
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(entity.getId())) {
			return Response.status(Status.CONFLICT).entity(entity).build();
		}
		if (service.findById(id) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			entity = service.update(id, entity);
		} catch (Exception e) {
			return Response.status(Response.Status.CONFLICT).entity(entity)
					.build();
		}

		return Response.noContent().build();
	}

}

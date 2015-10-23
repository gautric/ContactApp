package com.redhat.contacts.soap;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.redhat.contacts.model.Contact;
import com.redhat.contacts.service.ContactService;

/**
 * 
 */
@WebService
public class ContactWebService {

	@Inject
	ContactService service;

	@WebMethod
	public Contact create(Contact entity) throws Exception {
		return service.create(entity);
	}

	@WebMethod
	public void deleteById(Long id) {
		service.deleteById(id);
	}

	@WebMethod
	public Contact findById(Long id) {
		return service.findById(id);
	}

	@WebMethod
	public List<Contact> listAll(Integer startPosition, Integer maxResult) {
		return service.listAll(startPosition, maxResult);
	}

	@WebMethod
	public Contact update(Long id, Contact entity) {
		return service.update(id, entity);
	}
}

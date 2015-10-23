package com.redhat.contacts.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;


@Entity
@XmlRootElement
@Indexed
@Cacheable(value=true)
public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8112699446425848435L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

	@Column
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES) // Store.Yes for debug only
	private String nom;

	@Column
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	private String prenom;

	@Column
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	private String email;

	@Column
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	private String ville;

	@Column
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column
	private String grade;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Contact)) {
			return false;
		}
		Contact other = (Contact) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (nom != null && !nom.trim().isEmpty())
			result += "nom: " + nom;
		if (prenom != null && !prenom.trim().isEmpty())
			result += ", prenom: " + prenom;
		if (email != null && !email.trim().isEmpty())
			result += ", email: " + email;
		if (ville != null && !ville.trim().isEmpty())
			result += ", ville: " + ville;
		if (grade != null && !grade.trim().isEmpty())
			result += ", grade: " + grade;
		return result;
	}
}
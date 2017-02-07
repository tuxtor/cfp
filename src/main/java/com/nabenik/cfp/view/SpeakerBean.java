package com.nabenik.cfp.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.nabenik.cfp.model.Speaker;

/**
 * Backing bean for Speaker entities.
 * <p/>
 * This class provides CRUD functionality for all Speaker entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SpeakerBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Speaker entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Speaker speaker;

	public Speaker getSpeaker() {
		return this.speaker;
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "cfp-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.speaker = this.example;
		} else {
			this.speaker = findById(getId());
		}
	}

	public Speaker findById(Long id) {

		return this.entityManager.find(Speaker.class, id);
	}

	/*
	 * Support updating and deleting Speaker entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.speaker);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.speaker);
				return "view?faces-redirect=true&id=" + this.speaker.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			Speaker deletableEntity = findById(getId());

			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching Speaker entities with pagination
	 */

	private int page;
	private long count;
	private List<Speaker> pageItems;

	private Speaker example = new Speaker();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Speaker getExample() {
		return this.example;
	}

	public void setExample(Speaker example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Speaker> root = countCriteria.from(Speaker.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Speaker> criteria = builder.createQuery(Speaker.class);
		root = criteria.from(Speaker.class);
		TypedQuery<Speaker> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Speaker> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String firstname = this.example.getFirstname();
		if (firstname != null && !"".equals(firstname)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("firstname")),
					'%' + firstname.toLowerCase() + '%'));
		}
		String lastname = this.example.getLastname();
		if (lastname != null && !"".equals(lastname)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("lastname")),
					'%' + lastname.toLowerCase() + '%'));
		}
		String bio = this.example.getBio();
		if (bio != null && !"".equals(bio)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("bio")),
					'%' + bio.toLowerCase() + '%'));
		}
		String twitter = this.example.getTwitter();
		if (twitter != null && !"".equals(twitter)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("twitter")),
					'%' + twitter.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Speaker> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Speaker entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Speaker> getAll() {

		CriteriaQuery<Speaker> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Speaker.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Speaker.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final SpeakerBean ejbProxy = this.sessionContext
				.getBusinessObject(SpeakerBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Speaker) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Speaker add = new Speaker();

	public Speaker getAdd() {
		return this.add;
	}

	public Speaker getAdded() {
		Speaker added = this.add;
		this.add = new Speaker();
		return added;
	}
}

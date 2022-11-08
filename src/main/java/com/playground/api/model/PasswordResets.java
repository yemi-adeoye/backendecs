package com.playground.api.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
	@Table(name="password_resets")
	public class PasswordResets {
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private long id;

		private String rand;

		@ManyToOne
		private User user;

		private LocalDate generatedAt;

		public PasswordResets() {
			super();
			// TODO Auto-generated constructor stub
			this.generatedAt = LocalDate.now();
		}

		public PasswordResets(long id, String rand, User user, LocalDate generatedAt) {
			super();
			this.rand = rand;
			this.user = user;
			this.generatedAt = generatedAt;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getRand() {
			return rand;
		}

		public void setRand(String rand) {
			this.rand = rand;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public LocalDate getGeneratedAt() {
			return generatedAt;
		}

		public void setGeneratedAt(LocalDate generatedAt) {
			this.generatedAt = generatedAt;
		}





 
}

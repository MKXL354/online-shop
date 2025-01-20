# Online Shop

This is an online shop application server written in java. It is also tested for deployment in Tomcat

---

## Table of Contents

- [Usage](#usage)
- [Features](#features)

---

## Usage

You can easily clone the repository and resolve the dependencies using Maven. Be careful providing your own version of the dependecies as there might be conflicts.
After building the app, you use a Servlet Container to deploy it; Tomcat is recommended as it was tested during development. 

## Feature

This app features the basic functionalities of an online shop; such as web access, security, user, product, cart and payment management, transaction management, database connectivity.
In earlier commits most of these features were implemented manually and using core java libraries. In later commits a migration to Spring happened.
Now the app makes use of Spring Data JPA, Hibernate and WebMVC for development of the Data Access and Web layers

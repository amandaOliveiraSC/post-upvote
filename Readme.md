## Building a Full Stack Posts app similar to twitter posts with Spring Boot, Spring Security, JWT, React and Ant Design


## Steps to Setup the Spring Boot Back end app (upvote-app-server)

1. **Clone the application**

	```bash
	git clone git clone https://amandaoliveira@bitbucket.org/amandaoliveira/post-upvote.git
	cd upvote-app-server
	```

2. **Run the app**

	You can run the spring boot app by typing the following command -

	```bash
	mvn spring-boot:run
	```

	The server will start on port 5000. The spring boot app includes the front end build also, so you'll be able to access the complete application on `http://localhost:5000`.

	```


## Steps to Setup the React Front end app (upvote-app-client)

First go to the `upvote-app-client` folder -

```bash
cd upvote-app-client
```

Then type the following command to install the dependencies and start the application -

```bash
npm install && npm start
```

The front-end server will start on port `3000`.

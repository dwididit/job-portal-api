# Job Portal Application

This project is a RESTful API for a Job Portal application developed using Spring Boot and PostgreSQL. The application allows employers to post jobs, freelancers to apply for jobs, and provides various functionalities such as user authentication, job management, and proposal management.

## Features

- Create job entries
- Retrieve a specific job entry by ID
- Retrieve all job entries with optional pagination
- Retrieve jobs by status
- Retrieve jobs by employer
- Retrieve proposals by job
- Create proposals
- Update proposals
- Change proposal status to hired
- Retrieve proposals by freelancer
- Retrieve proposals by status
- Handle errors gracefully with custom exception handling
- Return data in a structured JSON format
- User authentication for employers and freelancers

# Entity Relationship Diagram (ERD)
![Logo](https://dwidi.com/wp-content/uploads/2024/06/Job-Portal-Database-Design.png)


# Database Schema Overview
### Table: `employer`
- **Purpose:** Manages employer information.
- **Description:** Stores contact information and company details of employers.
- **Key Columns:**
    - `employer_id` (BIGINT, PRIMARY KEY): Unique identifier for each employer. 
    - `company_name` (VARCHAR(255)): Name of the company.
    - `created_at` (TIMESTAMP(6) WITH TIME ZONE): Timestamp of account creation. 
    - `email` (VARCHAR(255)): Contact email address. 
    - `name` (VARCHAR(255)): Employer's full name. 
    - `updated_at` (TIMESTAMP(6) WITH TIME ZONE): Timestamp of last account update. 
    - `user_role` (VARCHAR(255)): Role of the user. 
    - `username` (VARCHAR(255)): Unique username for the employer. 
    - `password` (VARCHAR(255)): Encrypted password for the employer's account.

### Table: `freelancer`
- **Purpose:** Manages freelancer information.
- **Description:** Stores contact details and skillsets of freelancers.
- **Key Columns:** 
    - `freelancer_id` (BIGINT, PRIMARY KEY): Unique identifier for each freelancer. 
    - `created_at` (TIMESTAMP(6) WITH TIME ZONE): Timestamp of account creation. 
    - `email` (VARCHAR(255)): Contact email address. 
    - `name` (VARCHAR(255)): Freelancer's full name. 
    - `skills` (VARCHAR(255)): Comma-separated list of skills. 
    - `user_role` (VARCHAR(255)): Role of the user. 
    - `username` (VARCHAR(255)): Unique username for the freelancer. 
    - `updated_at` (TIMESTAMP(6) WITH TIME ZONE): Timestamp of last account update. 
    - `password` (VARCHAR(255)): Encrypted password for the freelancer's account.


### Table: `job`
- **Purpose:** Details job listings posted by employers.
- **Description:** Includes job specifications and status details.
- **Key Columns:**
    - `job_id` (BIGINT, PRIMARY KEY): Unique identifier for each job. 
    - `created_at` (TIMESTAMP(6) WITH TIME ZONE): Timestamp of job posting. 
    - `description` (VARCHAR(255)): Detailed job description. 
    - `status` (VARCHAR(255)): Current status of the job.
    - `title` (VARCHAR(255)): Job title. 
    - `employer_id` (BIGINT, FOREIGN KEY): Links to employer.employer_id. 
    - `updated_at` (TIMESTAMP(6) WITH TIME ZONE): Timestamp of last job update.


### Table: `proposal`
- **Purpose:** Records job proposals submitted by freelancers.
- **Description:** Includes proposal details and status.
- **Key Columns:**
    - `proposal_id` (BIGINT, PRIMARY KEY): Unique identifier for each proposal. 
    - `description` (VARCHAR(255)): Detailed proposal description. 
    - `title` (VARCHAR(255)): Proposal title. 
    - `updated_at` (TIMESTAMP(6) WITH TIME ZONE): Timestamp of last proposal update. 
    - `freelancer_id` (BIGINT, FOREIGN KEY): References freelancer.freelancer_id. 
    - `job_id` (BIGINT, FOREIGN KEY): References job.job_id. 
    - `created_at` (TIMESTAMP(6) WITH TIME ZONE): Timestamp of proposal submission. 
    - `status` (VARCHAR(255)): Current status of the proposal.


## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
- Docker
- Docker Compose
- Java 21
- Maven 3.9.6
- Spring Boot 3.3.0

### Installation

1. Clone the repository:
```bash
git@gitlab.com:dwididit/job-portal-api.git
cd job-portal-api/
``` 

2. Build .jar using Maven
```bash
mvn clean package
```

3. Build and run with Docker Compose
```bash
docker-compose up --build -d
```


The application will run on http://localhost:8080

For API documentation on Swagger, visit here: http://localhost:8080/swagger-ui/index.html

To make requests with Postman, visit here: https://documenter.getpostman.com/view/32199524/2sA3XQhh66

## User Authentication and Authorization
On this RESTful API, there are three roles:
1. ROLE_ADMIN
2. ROLE_EMPLOYER
3. ROLE_FREELANCER

Each role has specific permissions and access to different parts of the API. 

Authentication and authorization are handled using JWT (JSON Web Tokens).

### Authentication and Authorization Flow
1. Login:
Users (Admin, Employer, Freelancer) log in using their credentials (username and password).
A JWT token is generated and returned to the user upon successful authentication.

2. Accessing Protected Endpoints:
For every request to a protected endpoint, the JWT token must be included in the Authorization header as a Bearer token.
The server verifies the JWT token and extracts the user’s role.

3. Role-Based Access Control:
The server checks if the user’s role has the necessary permissions to access the requested endpoint.
If the user has the required role, the request is processed.
If the user does not have the required role, an authorization error is returned.

### Database Initialization
When building and running the application using Docker, the database automatically creates the following users:

1. Admin User
   - Username: admin
   - Password: password123

2. Employer User
   - Username: employer
   - Password: password123

3. Freelancer User
   - Username: freelancer
   - Password: password123







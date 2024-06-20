-- Create Employer table
CREATE TABLE IF NOT EXISTS employer (
                                        employer_id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    company_name VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    user_role VARCHAR(50) NOT NULL
    );

-- Create Freelancer table
CREATE TABLE IF NOT EXISTS freelancer (
                                          freelancer_id SERIAL PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    skills VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    user_role VARCHAR(50) NOT NULL
    );

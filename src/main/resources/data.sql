INSERT INTO skill (name) VALUES
                              ('Java'),
                              ('Python'),
                              ('JavaScript'),
                              ('C++'),
                              ('Ruby'),
                              ('C#'),
                              ('Go'),
                              ('Rust'),
                              ('Swift'),
                              ('Kotlin'),
                              ('PHP'),
                              ('SQL'),
                              ('HTML/CSS'),
                              ('TypeScript'),
                              ('Shell Scripting'),
                              ('R'),
                              ('React.js'),
                              ('Angular'),
                              ('Vue.js'),
                              ('Node.js'),
                              ('Django'),
                              ('Flask'),
                              ('Express.js'),
                              ('Spring Framework'),
                              ('Ruby on Rails'),
                              ('Laravel'),
                              ('Bootstrap'),
                              ('Tailwind CSS'),
                              ('jQuery'),
                              ('HTML5'),
                              ('CSS3'),
                              ('GraphQL'),
                              ('REST APIs'),
                              ('React Native'),
                              ('Flutter'),
                              ('Android Development'),
                              ('iOS Development'),
                              ('Xamarin'),
                              ('AWS'),
                              ('Azure'),
                              ('Google Cloud Platform'),
                              ('Kubernetes'),
                              ('Docker'),
                              ('Terraform'),
                              ('Jenkins'),
                              ('GitLab CI/CD'),
                              ('MySQL'),
                              ('PostgreSQL'),
                              ('MongoDB'),
                              ('Redis'),
                              ('Cassandra'),
                              ('SQLite'),
                              ('Oracle DB'),
                              ('Elasticsearch'),
                              ('MariaDB'),
                              ('DynamoDB'),
                              ('Machine Learning'),
                              ('Deep Learning'),
                              ('Artificial Intelligence'),
                              ('Data Analysis'),
                              ('Data Visualization'),
                              ('Pandas'),
                              ('NumPy'),
                              ('SciPy'),
                              ('TensorFlow'),
                              ('PyTorch'),
                              ('Scikit-learn'),
                              ('Jupyter Notebooks'),
                              ('Tableau'),
                              ('Power BI'),
                              ('Hadoop'),
                              ('Spark'),
                              ('Apache Kafka'),
                              ('Ethical Hacking'),
                              ('Penetration Testing'),
                              ('Cryptography'),
                              ('Network Security'),
                              ('Firewalls'),
                              ('Security Audits'),
                              ('Malware Analysis'),
                              ('Data Encryption'),
                              ('Cloud Security'),
                              ('OWASP'),
                              ('Security Incident Response'),
                              ('Sketch'),
                              ('Figma'),
                              ('Adobe XD'),
                              ('InVision'),
                              ('Photoshop'),
                              ('Illustrator'),
                              ('Wireframing'),
                              ('Prototyping'),
                              ('User Testing'),
                              ('Interaction Design'),
                              ('Visual Design'),
                              ('Agile Methodology'),
                              ('Scrum'),
                              ('Kanban'),
                              ('JIRA'),
                              ('Trello'),
                              ('Asana'),
                              ('MS Project'),
                              ('Product Management'),
                              ('Time Management'),
                              ('Risk Management'),
                              ('Budgeting'),
                              ('Digital Marketing'),
                              ('SEO'),
                              ('SEM'),
                              ('Content Marketing'),
                              ('Social Media Marketing'),
                              ('Market Research'),
                              ('Growth Hacking'),
                              ('Customer Relationship Management (CRM)'),
                              ('Google Analytics'),
                              ('Sales Strategies'),
                              ('Product Strategy'),
                              ('Communication'),
                              ('Leadership'),
                              ('Problem-solving'),
                              ('Creativity'),
                              ('Critical Thinking'),
                              ('Teamwork'),
                              ('Adaptability'),
                              ('Conflict Resolution'),
                              ('Emotional Intelligence'),
                              ('Negotiation'),
                              ('Presentation Skills');


-- Inserting 20 Jobs into the job table
INSERT INTO job (description, location, requirements, salary, title, company_id, recruiter_id, created_at, updated_at)
VALUES
    ('Develop new web applications and websites', 'New York, NY', 'Java, Spring Framework, PostgreSQL, AWS', 120000, 'Software Engineer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Design and implement databases and algorithms for scalable applications', 'San Francisco, CA', 'Python, PostgreSQL, Machine Learning, Docker', 130000, 'Data Scientist', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Develop mobile applications for iOS and Android', 'Los Angeles, CA', 'Swift, Kotlin, Android Development, iOS Development', 110000, 'Mobile App Developer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Build and maintain backend systems using Node.js', 'Chicago, IL', 'Node.js, Express.js, MongoDB, REST APIs', 115000, 'Backend Developer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Create interactive web applications using modern frameworks', 'Austin, TX', 'React.js, JavaScript, HTML5, CSS3', 100000, 'Frontend Developer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Design and implement cybersecurity solutions', 'Seattle, WA', 'Ethical Hacking, Penetration Testing, Network Security, Firewalls', 125000, 'Cybersecurity Engineer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Lead the product management team and drive development', 'Miami, FL', 'Product Management, Scrum, Agile Methodology', 140000, 'Product Manager', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Optimize and automate machine learning models', 'Boston, MA', 'TensorFlow, Machine Learning, Python, AWS', 135000, 'ML Engineer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Manage digital marketing campaigns and SEO efforts', 'Denver, CO', 'SEO, Google Analytics, SEM, Content Marketing', 95000, 'Digital Marketing Manager', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Develop and design software for enterprise clients', 'Dallas, TX', 'Java, C++, Spring Framework, AWS', 125000, 'Senior Software Engineer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Design and develop UI/UX for mobile applications', 'Austin, TX', 'Figma, Sketch, React Native, Flutter', 105000, 'UI/UX Designer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Maintain cloud infrastructure for enterprise systems', 'San Francisco, CA', 'AWS, Kubernetes, Docker, Terraform', 130000, 'Cloud Engineer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Write and test efficient SQL queries for data analytics', 'New York, NY', 'SQL, PostgreSQL, Data Analysis, Machine Learning', 95000, 'Data Analyst', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Build and deploy REST APIs using Flask and Node.js', 'Chicago, IL', 'Flask, Node.js, Express.js, REST APIs', 120000, 'API Developer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Manage AWS infrastructure and DevOps pipelines', 'Seattle, WA', 'AWS, Jenkins, Docker, Terraform', 115000, 'DevOps Engineer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Lead a team of software engineers for scalable solutions', 'Dallas, TX', 'Java, Spring Framework, Microservices, AWS', 140000, 'Engineering Manager', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Create mobile games using Unity and C#', 'Los Angeles, CA', 'Unity, C#, Mobile Game Development', 105000, 'Game Developer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Develop data visualization dashboards using Tableau', 'Boston, MA', 'Tableau, Data Visualization, SQL', 95000, 'Data Visualization Specialist', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Automate and optimize business processes using Python', 'Chicago, IL', 'Python, Automation, Scripting, PostgreSQL', 100000, 'Process Automation Engineer', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Research and implement cutting-edge technologies for AI', 'San Francisco, CA', 'Artificial Intelligence, Deep Learning, Python, TensorFlow', 145000, 'AI Researcher', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserting Skills for Jobs into the job_skills table
INSERT INTO job_skills (job_id, skill_id)
VALUES
    (7, 7),  -- Software Engineer -> Java
    (7, 28), -- Software Engineer -> Spring Framework
    (7, 51), -- Software Engineer -> PostgreSQL
    (7, 43), -- Software Engineer -> AWS
    (8, 8),  -- Data Scientist -> Python
    (8, 51), -- Data Scientist -> PostgreSQL
    (8, 61), -- Data Scientist -> Machine Learning
    (8, 47), -- Data Scientist -> Docker
    (9, 14), -- Mobile App Developer -> Swift
    (9, 15), -- Mobile App Developer -> Kotlin
    (9, 40), -- Mobile App Developer -> Android Development
    (9, 41), -- Mobile App Developer -> iOS Development
    (10, 18), -- Backend Developer -> Node.js
    (10, 27), -- Backend Developer -> Express.js
    (10, 53), -- Backend Developer -> MongoDB
    (10, 37), -- Backend Developer -> REST APIs
    (11, 21), -- Frontend Developer -> React.js
    (11, 7),  -- Frontend Developer -> JavaScript
    (11, 34), -- Frontend Developer -> HTML5
    (11, 35), -- Frontend Developer -> CSS3
    (12, 78), -- Cybersecurity Engineer -> Ethical Hacking
    (12, 79), -- Cybersecurity Engineer -> Penetration Testing
    (12, 81), -- Cybersecurity Engineer -> Network Security
    (12, 82), -- Cybersecurity Engineer -> Firewalls
    (13, 107), -- Product Manager -> Product Management
    (13, 101), -- Product Manager -> Scrum
    (13, 100), -- Product Manager -> Agile Methodology
    (14, 69), -- ML Engineer -> TensorFlow
    (14, 61), -- ML Engineer -> Machine Learning
    (14, 8),  -- ML Engineer -> Python
    (14, 43), -- ML Engineer -> AWS
    (15, 112), -- Digital Marketing Manager -> SEO
    (15, 119), -- Digital Marketing Manager -> Google Analytics
    (15, 113), -- Digital Marketing Manager -> SEM
    (15, 114), -- Digital Marketing Manager -> Content Marketing
    (16, 1),  -- Senior Software Engineer -> Java
    (16, 8),  -- Senior Software Engineer -> C++
    (16, 28), -- Senior Software Engineer -> Spring Framework
    (16, 43); -- Senior Software Engineer -> AWS

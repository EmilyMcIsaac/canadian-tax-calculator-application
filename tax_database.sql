--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

-- Started on 2024-09-23 20:09:22

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4896 (class 1262 OID 25100)
-- Name: tax_db; Type: DATABASE; Schema: -; Owner: emily
--

CREATE DATABASE tax_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';


\connect tax_db

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 224 (class 1259 OID 25278)
-- Name: basic_personal_amounts; Type: TABLE; Schema: public; Owner: emily
--

CREATE TABLE public.basic_personal_amounts (
    bpa_id bigint NOT NULL,
    region character varying(255),
    year integer,
    bpa_amount numeric(38,2),
    bpa_phaseout_start numeric(38,2),
    bpa_phaseout_end numeric(38,2)
);

--
-- TOC entry 223 (class 1259 OID 25277)
-- Name: basic_personal_amounts_bpa_id_seq; Type: SEQUENCE; Schema: public; Owner: emily
--

CREATE SEQUENCE public.basic_personal_amounts_bpa_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE public.tax_brackets (
    bracket_id bigint NOT NULL,
    region character varying(255) NOT NULL,
    year integer NOT NULL,
    min_income numeric(38,2) NOT NULL,
    max_income numeric(38,2) NOT NULL,
    tax_rate numeric(38,2) NOT NULL,
    CONSTRAINT tax_brackets_check CHECK ((min_income < max_income)),
    CONSTRAINT tax_brackets_tax_rate_check CHECK (((tax_rate > (0)::numeric) AND (tax_rate <= (1)::numeric)))
);

--
-- TOC entry 217 (class 1259 OID 25116)
-- Name: tax_brackets_bracket_id_seq; Type: SEQUENCE; Schema: public; Owner: emily
--

CREATE SEQUENCE public.tax_brackets_bracket_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4898 (class 0 OID 0)
-- Dependencies: 217
-- Name: tax_brackets_bracket_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: emily
--

CREATE TABLE public.tax_calculations (
    calculation_id bigint NOT NULL,
    user_id bigint,
    income numeric(38,2) NOT NULL,
    region character varying(255) NOT NULL,
    federal_tax numeric(38,2) NOT NULL,
    provincial_tax numeric(38,2) NOT NULL,
    net_income numeric(38,2) NOT NULL,
    calculation_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    saved boolean DEFAULT false,
    deleted boolean DEFAULT false,
    tax_year integer DEFAULT EXTRACT(year FROM CURRENT_DATE) NOT NULL,
    CONSTRAINT tax_calculations_income_check CHECK ((income > (0)::numeric))
);


--
-- TOC entry 219 (class 1259 OID 25125)
-- Name: tax_calculations_calculation_id_seq; Type: SEQUENCE; Schema: public; Owner: emily
--

CREATE SEQUENCE public.tax_calculations_calculation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4899 (class 0 OID 0)
-- Dependencies: 219
-- Name: tax_calculations_calculation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: emily
--


CREATE TABLE public.tax_history (
    history_id bigint NOT NULL,
    calculation_id bigint,
    saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

--
-- TOC entry 221 (class 1259 OID 25142)
-- Name: tax_history_history_id_seq; Type: SEQUENCE; Schema: public; Owner: emily
--

CREATE SEQUENCE public.tax_history_history_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4900 (class 0 OID 0)
-- Dependencies: 221
-- Name: tax_history_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: emily
--

CREATE TABLE public.users (
    user_id bigint NOT NULL,
    username character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    password_hash character varying(255) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT users_password_hash_check CHECK ((length((password_hash)::text) >= 8))
);


--
-- TOC entry 215 (class 1259 OID 25101)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: emily
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4713 (class 2604 OID 25284)
-- Name: basic_personal_amounts bpa_id; Type: DEFAULT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.basic_personal_amounts ALTER COLUMN bpa_id SET DEFAULT nextval('public.basic_personal_amounts_bpa_id_seq'::regclass);


--
-- TOC entry 4705 (class 2604 OID 25189)
-- Name: tax_brackets bracket_id; Type: DEFAULT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.tax_brackets ALTER COLUMN bracket_id SET DEFAULT nextval('public.tax_brackets_bracket_id_seq'::regclass);


--
-- TOC entry 4706 (class 2604 OID 25203)
-- Name: tax_calculations calculation_id; Type: DEFAULT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.tax_calculations ALTER COLUMN calculation_id SET DEFAULT nextval('public.tax_calculations_calculation_id_seq'::regclass);


--
-- TOC entry 4711 (class 2604 OID 25228)
-- Name: tax_history history_id; Type: DEFAULT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.tax_history ALTER COLUMN history_id SET DEFAULT nextval('public.tax_history_history_id_seq'::regclass);


--
-- TOC entry 4703 (class 2604 OID 25247)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- TOC entry 4890 (class 0 OID 25278)
-- Dependencies: 224
-- Data for Name: basic_personal_amounts; Type: TABLE DATA; Schema: public; Owner: emily
--

INSERT INTO public.basic_personal_amounts VALUES (1, 'federal', 2024, 15000.00, 150000.00, 221708.00);
INSERT INTO public.basic_personal_amounts VALUES (2, 'ontario', 2024, 11865.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (3, 'british columbia', 2024, 11981.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (4, 'alberta', 2024, 21003.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (5, 'quebec', 2024, 18056.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (6, 'manitoba', 2024, 10855.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (7, 'saskatchewan', 2024, 16065.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (8, 'nova scotia', 2024, 8481.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (9, 'new brunswick', 2024, 12458.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (10, 'newfoundland and labrador', 2024, 10382.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (11, 'prince edward island', 2024, 12000.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (12, 'yukon', 2024, 15200.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (13, 'northwest territories', 2024, 15980.00, NULL, NULL);
INSERT INTO public.basic_personal_amounts VALUES (14, 'nunavut', 2024, 16672.00, NULL, NULL);


--
-- TOC entry 4884 (class 0 OID 25117)
-- Dependencies: 218
-- Data for Name: tax_brackets; Type: TABLE DATA; Schema: public; Owner: emily
--

INSERT INTO public.tax_brackets VALUES (1, 'federal', 2024, 0.00, 55867.00, 0.15);
INSERT INTO public.tax_brackets VALUES (2, 'federal', 2024, 55867.01, 111733.00, 0.21);
INSERT INTO public.tax_brackets VALUES (3, 'federal', 2024, 111733.01, 173205.00, 0.26);
INSERT INTO public.tax_brackets VALUES (4, 'federal', 2024, 173205.01, 246752.00, 0.29);
INSERT INTO public.tax_brackets VALUES (5, 'federal', 2024, 246752.01, 999999999.99, 0.33);
INSERT INTO public.tax_brackets VALUES (6, 'newfoundland and labrador', 2024, 0.00, 43198.00, 0.09);
INSERT INTO public.tax_brackets VALUES (7, 'newfoundland and labrador', 2024, 43198.01, 86395.00, 0.15);
INSERT INTO public.tax_brackets VALUES (8, 'newfoundland and labrador', 2024, 86395.01, 154244.00, 0.16);
INSERT INTO public.tax_brackets VALUES (9, 'newfoundland and labrador', 2024, 154244.01, 215943.00, 0.18);
INSERT INTO public.tax_brackets VALUES (10, 'newfoundland and labrador', 2024, 215943.01, 275870.00, 0.20);
INSERT INTO public.tax_brackets VALUES (11, 'newfoundland and labrador', 2024, 275870.01, 551739.00, 0.21);
INSERT INTO public.tax_brackets VALUES (12, 'newfoundland and labrador', 2024, 551739.01, 1103478.00, 0.21);
INSERT INTO public.tax_brackets VALUES (13, 'newfoundland and labrador', 2024, 1103478.01, 999999999.99, 0.22);
INSERT INTO public.tax_brackets VALUES (14, 'prince edward island', 2024, 0.00, 32656.00, 0.10);
INSERT INTO public.tax_brackets VALUES (15, 'prince edward island', 2024, 32656.01, 64313.00, 0.14);
INSERT INTO public.tax_brackets VALUES (16, 'prince edward island', 2024, 64313.01, 105000.00, 0.17);
INSERT INTO public.tax_brackets VALUES (17, 'prince edward island', 2024, 105000.01, 140000.00, 0.18);
INSERT INTO public.tax_brackets VALUES (18, 'prince edward island', 2024, 140000.01, 999999999.99, 0.19);
INSERT INTO public.tax_brackets VALUES (19, 'nova scotia', 2024, 0.00, 29590.00, 0.09);
INSERT INTO public.tax_brackets VALUES (20, 'nova scotia', 2024, 29590.01, 59180.00, 0.15);
INSERT INTO public.tax_brackets VALUES (21, 'nova scotia', 2024, 59180.01, 93000.00, 0.17);
INSERT INTO public.tax_brackets VALUES (22, 'nova scotia', 2024, 93000.01, 150000.00, 0.18);
INSERT INTO public.tax_brackets VALUES (23, 'nova scotia', 2024, 150000.01, 999999999.99, 0.21);
INSERT INTO public.tax_brackets VALUES (24, 'new brunswick', 2024, 0.00, 49958.00, 0.09);
INSERT INTO public.tax_brackets VALUES (25, 'new brunswick', 2024, 49958.01, 99916.00, 0.14);
INSERT INTO public.tax_brackets VALUES (26, 'new brunswick', 2024, 99916.01, 185064.00, 0.16);
INSERT INTO public.tax_brackets VALUES (27, 'new brunswick', 2024, 185064.01, 999999999.99, 0.19);
INSERT INTO public.tax_brackets VALUES (28, 'quebec', 2024, 0.00, 51780.00, 0.14);
INSERT INTO public.tax_brackets VALUES (29, 'quebec', 2024, 51780.01, 103545.00, 0.19);
INSERT INTO public.tax_brackets VALUES (30, 'quebec', 2024, 103545.01, 126000.00, 0.24);
INSERT INTO public.tax_brackets VALUES (31, 'quebec', 2024, 126000.01, 999999999.99, 0.26);
INSERT INTO public.tax_brackets VALUES (32, 'ontario', 2024, 0.00, 51446.00, 0.05);
INSERT INTO public.tax_brackets VALUES (33, 'ontario', 2024, 51446.01, 102894.00, 0.09);
INSERT INTO public.tax_brackets VALUES (34, 'ontario', 2024, 102894.01, 150000.00, 0.11);
INSERT INTO public.tax_brackets VALUES (35, 'ontario', 2024, 150000.01, 220000.00, 0.12);
INSERT INTO public.tax_brackets VALUES (36, 'ontario', 2024, 220000.01, 999999999.99, 0.13);
INSERT INTO public.tax_brackets VALUES (37, 'manitoba', 2024, 0.00, 47000.00, 0.11);
INSERT INTO public.tax_brackets VALUES (38, 'manitoba', 2024, 47000.01, 100000.00, 0.13);
INSERT INTO public.tax_brackets VALUES (39, 'manitoba', 2024, 100000.01, 999999999.99, 0.17);
INSERT INTO public.tax_brackets VALUES (40, 'saskatchewan', 2024, 0.00, 52057.00, 0.11);
INSERT INTO public.tax_brackets VALUES (41, 'saskatchewan', 2024, 52057.01, 148734.00, 0.13);
INSERT INTO public.tax_brackets VALUES (42, 'saskatchewan', 2024, 148734.01, 999999999.99, 0.15);
INSERT INTO public.tax_brackets VALUES (43, 'alberta', 2024, 0.00, 148269.00, 0.10);
INSERT INTO public.tax_brackets VALUES (44, 'alberta', 2024, 148269.01, 177922.00, 0.12);
INSERT INTO public.tax_brackets VALUES (45, 'alberta', 2024, 177922.01, 237230.00, 0.13);
INSERT INTO public.tax_brackets VALUES (46, 'alberta', 2024, 237230.01, 355845.00, 0.14);
INSERT INTO public.tax_brackets VALUES (47, 'alberta', 2024, 355845.01, 999999999.99, 0.15);
INSERT INTO public.tax_brackets VALUES (48, 'british columbia', 2024, 0.00, 47937.00, 0.05);
INSERT INTO public.tax_brackets VALUES (49, 'british columbia', 2024, 47937.01, 95875.00, 0.08);
INSERT INTO public.tax_brackets VALUES (50, 'british columbia', 2024, 95875.01, 110076.00, 0.11);
INSERT INTO public.tax_brackets VALUES (51, 'british columbia', 2024, 110076.01, 133664.00, 0.12);
INSERT INTO public.tax_brackets VALUES (52, 'british columbia', 2024, 133664.01, 181232.00, 0.15);
INSERT INTO public.tax_brackets VALUES (53, 'british columbia', 2024, 181232.01, 252752.00, 0.17);
INSERT INTO public.tax_brackets VALUES (54, 'british columbia', 2024, 252752.01, 999999999.99, 0.21);
INSERT INTO public.tax_brackets VALUES (55, 'yukon', 2024, 0.00, 55867.00, 0.06);
INSERT INTO public.tax_brackets VALUES (56, 'yukon', 2024, 55867.01, 111733.00, 0.09);
INSERT INTO public.tax_brackets VALUES (57, 'yukon', 2024, 111733.01, 173205.00, 0.11);
INSERT INTO public.tax_brackets VALUES (58, 'yukon', 2024, 173205.01, 500000.00, 0.13);
INSERT INTO public.tax_brackets VALUES (59, 'yukon', 2024, 500000.01, 999999999.99, 0.15);
INSERT INTO public.tax_brackets VALUES (60, 'northwest territories', 2024, 0.00, 50597.00, 0.06);
INSERT INTO public.tax_brackets VALUES (61, 'northwest territories', 2024, 50597.01, 101198.00, 0.09);
INSERT INTO public.tax_brackets VALUES (62, 'northwest territories', 2024, 101198.01, 164525.00, 0.12);
INSERT INTO public.tax_brackets VALUES (63, 'northwest territories', 2024, 164525.01, 999999999.99, 0.14);
INSERT INTO public.tax_brackets VALUES (64, 'nunavut', 2024, 0.00, 53268.00, 0.04);
INSERT INTO public.tax_brackets VALUES (65, 'nunavut', 2024, 53268.01, 106537.00, 0.07);
INSERT INTO public.tax_brackets VALUES (66, 'nunavut', 2024, 106537.01, 173205.00, 0.09);
INSERT INTO public.tax_brackets VALUES (67, 'nunavut', 2024, 173205.01, 999999999.99, 0.12);


--
-- TOC entry 4735 (class 2606 OID 25286)
-- Name: basic_personal_amounts basic_personal_amounts_pkey; Type: CONSTRAINT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.basic_personal_amounts
    ADD CONSTRAINT basic_personal_amounts_pkey PRIMARY KEY (bpa_id);


--
-- TOC entry 4727 (class 2606 OID 25191)
-- Name: tax_brackets tax_brackets_pkey; Type: CONSTRAINT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.tax_brackets
    ADD CONSTRAINT tax_brackets_pkey PRIMARY KEY (bracket_id);


--
-- TOC entry 4730 (class 2606 OID 25205)
-- Name: tax_calculations tax_calculations_pkey; Type: CONSTRAINT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.tax_calculations
    ADD CONSTRAINT tax_calculations_pkey PRIMARY KEY (calculation_id);


--
-- TOC entry 4733 (class 2606 OID 25230)
-- Name: tax_history tax_history_pkey; Type: CONSTRAINT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.tax_history
    ADD CONSTRAINT tax_history_pkey PRIMARY KEY (history_id);


--
-- TOC entry 4721 (class 2606 OID 25269)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4723 (class 2606 OID 25249)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 4725 (class 2606 OID 25274)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 4728 (class 1259 OID 25217)
-- Name: idx_tax_calculations_user_id; Type: INDEX; Schema: public; Owner: emily
--

CREATE INDEX idx_tax_calculations_user_id ON public.tax_calculations USING btree (user_id);


--
-- TOC entry 4731 (class 1259 OID 25236)
-- Name: idx_tax_history_calculation_id; Type: INDEX; Schema: public; Owner: emily
--

CREATE INDEX idx_tax_history_calculation_id ON public.tax_history USING btree (calculation_id);


--
-- TOC entry 4718 (class 1259 OID 25270)
-- Name: idx_users_email; Type: INDEX; Schema: public; Owner: emily
--

CREATE INDEX idx_users_email ON public.users USING btree (email);


--
-- TOC entry 4719 (class 1259 OID 25275)
-- Name: idx_users_username; Type: INDEX; Schema: public; Owner: emily
--

CREATE INDEX idx_users_username ON public.users USING btree (username);


--
-- TOC entry 4736 (class 2606 OID 25255)
-- Name: tax_calculations tax_calculations_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.tax_calculations
    ADD CONSTRAINT tax_calculations_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- TOC entry 4737 (class 2606 OID 25237)
-- Name: tax_history tax_history_calculation_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: emily
--

ALTER TABLE ONLY public.tax_history
    ADD CONSTRAINT tax_history_calculation_id_fkey FOREIGN KEY (calculation_id) REFERENCES public.tax_calculations(calculation_id) ON DELETE CASCADE;


-- Completed on 2024-09-23 20:09:22

--
-- PostgreSQL database dump complete
--


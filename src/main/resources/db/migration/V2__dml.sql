insert into sentryc_interview.marketplaces (id, description) values ('marketplace1', 'description1');
insert into sentryc_interview.marketplaces (id, description) values ('marketplace2', 'description2');
-- -----------------------------------------------------------
insert into sentryc_interview.seller_infos (id, marketplace_id, name, url, country, external_id) values ('0fdf28a6-3368-4ba8-b9bc-fe1a583770b6', 'marketplace1', 'name1', 'url1', 'DE', 'id1');
insert into sentryc_interview.seller_infos (id, marketplace_id, name, url, country, external_id) values ('e1966fbf-fd29-45ee-a13d-4372f5291f6d', 'marketplace2', 'name2', 'url2', 'BE', 'id2');
-- ---------------------------------------------------
insert into sentryc_interview.producers (id, name, created_at) values ('e81a72fa-2a0c-42cf-9a5f-bfe44c95356b', 'name1', '2024-03-01 00:00:00.000000');
insert into sentryc_interview.producers (id, name, created_at) values ('0f4f0ea4-e369-4bc8-b343-3755da1222a3', 'name2', '2023-10-20 00:00:00.000000');
-- -----------------------------------------------------------
insert into sentryc_interview.sellers (id, producer_id, seller_info_id, state) values ('ee11cfa9-8c9f-41d0-ac48-fc46691e05e8', 'e81a72fa-2a0c-42cf-9a5f-bfe44c95356b', '0fdf28a6-3368-4ba8-b9bc-fe1a583770b6', 'REGULAR');
insert into sentryc_interview.sellers (id, producer_id, seller_info_id, state) values ('0bca887c-5c19-4644-9c6d-7428561a9f6a', '0f4f0ea4-e369-4bc8-b343-3755da1222a3', 'e1966fbf-fd29-45ee-a13d-4372f5291f6d', 'WHITELISTED');

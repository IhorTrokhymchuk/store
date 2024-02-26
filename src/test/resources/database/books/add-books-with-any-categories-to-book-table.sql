INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'TestBookTitle1', 'TestBookAuthor1', 'TestBookIsbn1', 100, 'TestBookDescription1', 'TestCoverImage1', FALSE);
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (2, 'TestBookTitle2', 'TestBookAuthor2', 'TestBookIsbn2', 100, 'TestBookDescription2', 'TestCoverImage2', FALSE);
INSERT INTO books_categories (book_id, category_id) VALUES (2, 1);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (3, 'TestBookTitle3', 'TestBookAuthor3', 'TestBookIsbn3', 100, 'TestBookDescription3', 'TestCoverImage3', FALSE);
INSERT INTO books_categories (book_id, category_id) VALUES (3, 2);

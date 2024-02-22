INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'TestBookTitle', 'TestBookAuthor', 'TestBookIsbn_1', 100, 'TestBookDescription', 'TestCoverImage', FALSE);
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);

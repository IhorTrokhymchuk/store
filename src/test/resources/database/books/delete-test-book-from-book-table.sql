DELETE FROM books_categories WHERE book_id IN (SELECT id FROM books WHERE title = 'TestBookTitle3' AND author = 'TestBookAuthor3' AND isbn = 'TestBookIsbn3' AND price = 100);
DELETE FROM books WHERE title = 'TestBookTitle3' AND author = 'TestBookAuthor3' AND isbn = 'TestBookIsbn3' AND price = 100;

SELECT book.name
FROM book
INNER JOIN author ON book.author_id = author.id
WHERE author.name = 'имяАвтора';


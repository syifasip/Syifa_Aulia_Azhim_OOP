public class Main {
    public static void main(String[] args) {
        Author authorWibu = new Author("Eiichiro Oda", 1975);
        Author authorKalcer = new Author("Yuval Noah Harari", 1976);
        Author authorDuduk = new Author("Stephen Hawking", 1942);
        Buku bukuOnePiece = new Buku("One Piece", 1997, BookGenre.Fiksi, authorWibu);
        Buku bukuSapiens = new Buku("Sapiens", 2011, BookGenre.Sejarah, authorKalcer);
        Buku bukuUniverse = new Buku("A Brief History of Time", 1988, BookGenre.Sains, authorDuduk);
        bukuOnePiece.showDetail();
        bukuSapiens.showDetail();
        bukuUniverse.showDetail();

    }


}
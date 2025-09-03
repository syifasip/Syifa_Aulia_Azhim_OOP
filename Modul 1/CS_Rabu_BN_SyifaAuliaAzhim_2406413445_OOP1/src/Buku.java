public class Buku {
    public String buku;
    public int year;

    public BookGenre genre;
    public Author author;

    public Buku(String buku, int year, BookGenre genre, Author author) {
        this.buku = buku;
        this.year = year;
        this.genre = genre;
        this.author = author;
    }

    public int getAuthorCurrentAge() {
        int currentAge = year - author.tahunLahir;
        return currentAge;
    }

    public void showDetail() {
        System.out.println("Judul: "+buku);
        System.out.println("Genre: "+genre);
        System.out.println("Tahun Terbit: "+year);
        System.out.println("Nama Author: "+author.nama);
        System.out.println("Tahun Lahir: "+author.tahunLahir);
        System.out.println("Usia Author Saat Buku Diterbitkan: "+getAuthorCurrentAge() +" tahun" +"\n");
    }

}

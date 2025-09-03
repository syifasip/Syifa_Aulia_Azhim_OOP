public class Author {
    public String nama;
    public int tahunLahir;

    public Author(String nama, int tahunLahir) {
        this.nama = nama;
        this.tahunLahir = tahunLahir;
    }

    public void showDetail(){
        System.out.println("Nama Author: "+nama);
        System.out.println("Tahun Lahir: "+tahunLahir);
    }
}

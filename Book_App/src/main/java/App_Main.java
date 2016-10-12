import javax.management.*;
import javax.persistence.*;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Богдан on 11.10.2016.
 */
public class App_Main {

    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try{
            emf = Persistence.createEntityManagerFactory("JPABook");
            em = emf.createEntityManager();
            try{
                while(true){
                    System.out.println("1: add book");
                    System.out.println("2: edit book");
                    System.out.println("3: remove book");
                    System.out.println("4: view books");
                    System.out.println("->");

                    String s = sc.nextLine();

                    switch(s){
                        case "1":
                            addBook(sc);
                            break;
                        case "2":
                            editBook(sc);
                            break;
                        case "3":
                            removeBook(sc);
                            break;
                        case "4":
                            viewBooks();
                            break;
                        default:
                            return;
                    }
                }
            }finally {
                sc.close();
                em.close();
                emf.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return;
        }
    }

    private static void addBook(Scanner sc){
        System.out.print("Enter book name: ");
        String bookname = sc.nextLine();

        System.out.print("Enter book author: ");
        String author = sc.nextLine();

        em.getTransaction().begin();
        try{
            Book b = new Book(bookname,author);
            em.persist(b);
            em.getTransaction().commit();
        }catch (Exception ex){
            em.getTransaction().rollback();
        }
        System.out.println("Book \""+bookname+"\" "+author+" was added");
    }

    private static void removeBook(Scanner sc){
        System.out.println("Enter book id: ");
        String sId = sc.nextLine();
        long id = Long.parseLong(sId);

        Book b = em.find(Book.class, id);
        if (b==null){
            System.out.println("Book not found!");
            return;
        }

        em.getTransaction().begin();
        try{
            em.remove(b);
            em.getTransaction().commit();
        }catch (Exception ex){
            em.getTransaction().rollback();
        }
        System.out.println("Book \"" + b.getBookname() +"\" "+b.getAuthor()+ " was removed");
    }

    private static void editBook(Scanner sc){
        System.out.println("Enter book name: ");
        String bookname = sc.nextLine();
        Book b = null;
        b = isUniqueBookname(bookname);

        System.out.println("Enter new book name");
        String newbookname = sc.nextLine();


//        try{
//            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.bookname = :bookname", Book.class);
//            query.setParameter("bookname",bookname);
//            b = (Book)query.getSingleResult();
//        }catch (NoResultException ex){
//            System.out.println("Book not found!");
//            return;
//        }catch (NonUniqueResultException ex){
//            System.out.println("Non unique result!");
//        }

        em.getTransaction().begin();
        try{
            b.setBookname(newbookname);
            em.getTransaction().commit();
        }catch (Exception ex){
            em.getTransaction().rollback();
        }
        System.out.println("Book with old bookname \""+ bookname + "\" was modified to \""+newbookname+"\"");
    }

    private static Book isUniqueBookname(String bookname){
        Book b1 = null;
        List<Book> b = new ArrayList<>();

        try{
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.bookname = :bookname", Book.class);
            query.setParameter("bookname",bookname);
            b = (List<Book>)query.getResultList();
        }catch (NoResultException ex){
            System.out.println("Book not found!");
            return  b1;
        }
        int i=1;
        if ((b.size()) != 1){
            for (Book bk : b){
                System.out.println( i++ + " "+bk.toString());
            }
            System.out.println("Choose book ->");
            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();
            int num = Integer.parseInt(s);
            b1 = b.get(num-1);
            //bookname = b.get(num-1).getBookname();

        }
        return  b1;
    }

    private static void viewBooks(){
        Query query = em.createQuery("SELECT b FROM Book b", Book.class);
        List<Book> list = (List<Book>)query.getResultList();

        for (Book b:list){
            System.out.println(b);
        }
    }


}

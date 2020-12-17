package com.example.thesis.backend.security.context;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.notice.*;
import com.example.thesis.backend.reservation.Property;
import com.example.thesis.backend.reservation.PropertyRepository;
import com.example.thesis.backend.reservation.Reservation;
import com.example.thesis.backend.reservation.ReservationRepository;
import com.example.thesis.backend.security.auth.Role;
import com.example.thesis.backend.security.auth.User;
import com.example.thesis.backend.security.auth.UserRepository;
import com.example.thesis.backend.security.utilities.DefaultPrivilegeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final FloorRepository floorRepository;
    private final PropertyRepository propertyRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;
    private final DefaultPrivilegeProvider privilegeProvider;
    private final NoticeBoardRepository noticeBoardRepository;
    private final CommentRepository commentRepository;


    @Autowired
    public SetupDataLoader(UserRepository userRepository, NoticeRepository noticeRepository,
                           FloorRepository floorRepository, PropertyRepository propertyRepository,
                           ReservationRepository reservationRepository, PasswordEncoder passwordEncoder,
                           DefaultPrivilegeProvider privilegeProvider, NoticeBoardRepository noticeBoardRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.noticeRepository = noticeRepository;
        this.floorRepository = floorRepository;
        this.propertyRepository = propertyRepository;
        this.reservationRepository = reservationRepository;
        this.passwordEncoder = passwordEncoder;
        this.privilegeProvider = privilegeProvider;
        this.noticeBoardRepository = noticeBoardRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("sdad@Sadasd.com");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setUsername("admin");
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        admin.setEnabled(true);
        Set<Role> roles = new HashSet<>();
        roles.add(privilegeProvider.admin("admin"));
        roles.add(privilegeProvider.user("admin"));
        admin.setRoles(roles);
        admin.setEnabled(true);
        userRepository.save(admin);

        User user = new User();
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("sdad@Sadasd.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setUsername("user");
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        Set<Role> roles2 = new HashSet<>();
        roles2.add(privilegeProvider.user("user"));
        user.setRoles(roles2);
        userRepository.save(user);

        File file = new File("testImage.jpg");
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String longArticle = "\"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, " +
                "totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae" +
                " dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, " +
                "sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam" +
                " est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius " +
                "modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam," +
                " quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? " +
                "Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel" +
                " illum qui dolorem eum fugiat quo voluptas nulla pariatur?\"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, tv";


        ParentComment comment1 = new ParentComment(user, Instant.now().minus(5, ChronoUnit.HOURS), "ParentComment");
        Comment comment2 = new Comment(user, Instant.now(), "Reply1");
        commentRepository.save(comment2);
        comment1.addReply(comment2);
        Comment comment3 = new Comment(user, Instant.now(), "Reply2");
        commentRepository.save(comment3);
        comment1.addReply(comment3);
        Comment comment8 = new Comment(user, Instant.now(), "Reply3");
        commentRepository.save(comment8);
        comment1.addReply(comment8);
        Comment comment9 = new Comment(user, Instant.now(), "Reply4");
        commentRepository.save(comment9);
        comment1.addReply(comment9);
        Comment comment10 = new Comment(user, Instant.now(), "Reply5");
        commentRepository.save(comment10);
        comment1.addReply(comment10);
        Comment comment11 = new Comment(user, Instant.now(), "Reply6");
        commentRepository.save(comment11);
        comment1.addReply(comment11);

        commentRepository.save(comment1);


        ParentComment comment4 = new ParentComment(user, Instant.now(), "ParentComment2nd");
        commentRepository.save(comment4);
        Comment comment5 = new Comment(user, Instant.now(), "Reply1-2nd");
        commentRepository.save(comment5);
        comment4.addReply(comment5);
        Comment comment6 = new Comment(user, Instant.now(), "Reply2-2nd");
        commentRepository.save(comment6);
        comment4.addReply(comment6);
        Comment comment7 = new Comment(user, Instant.now(), "Reply3-2nd");
        commentRepository.save(comment7);
        comment4.addReply(comment7);

        HashSet<ParentComment> comments = new HashSet<>();
        comments.add(comment1);
        comments.add(comment4);

        HashSet<ParentComment> comments2 = new HashSet<>();
//        comments2.add(comment4);

        Notice test_notice_one = Notice.builder().title("Ten jest z komentarzami").body(longArticle).creationDate(Instant.now())
                .image(bFile).parentComments(comments).build();
        Notice test_notice_two = Notice.builder().title("Ten jest bez komentarzy").body(longArticle).creationDate(Instant.now())
                .image(bFile).parentComments(comments2).build();
        noticeRepository.save(test_notice_one);
        noticeRepository.save(test_notice_two);

        Floor main = Floor.builder().name("Main").build();
        Floor floor = Floor.builder().name("1st Floor").build();
        Floor floor2 = Floor.builder().name("2nd Floor").build();

        user.addFloor(main);
        user.addFloor(floor);

        admin.addFloor(main);
        admin.addFloor(floor);

        NoticeBoard mainBoard = new NoticeBoard("Main Board", main);
        mainBoard.addNotice(test_notice_one);
        mainBoard.addNotice(test_notice_two);

        NoticeBoard floorOneBoard = new NoticeBoard("1st Floor", floor);
        floorOneBoard.addNotice(test_notice_one);
        floorOneBoard.addNotice(test_notice_two);

        noticeBoardRepository.save(mainBoard);
        noticeBoardRepository.save(floorOneBoard);


        floorRepository.save(main);
        floorRepository.save(floor);
        floorRepository.save(floor2);

        Property property = Property.builder().name("Laundry Room").owner(floor).build();
        Property property2 = Property.builder().name("Laundry Room").owner(floor2).build();

        propertyRepository.save(property);
        propertyRepository.save(property2);

        Reservation reservation = Reservation.builder().start(LocalDateTime.now()).duration(Duration.ofHours(1)).property(property).user(user).creationTime(Instant.now()).build();
        Reservation reservation3 = Reservation.builder().start(LocalDateTime.now().minusHours(3)).duration(Duration.ofHours(1)).property(property).user(user).creationTime(Instant.now()).build();
        Reservation reservation4 = Reservation.builder().start(LocalDateTime.now().minusHours(6)).duration(Duration.ofHours(1)).property(property).user(user).creationTime(Instant.now()).build();
        Reservation reservation2 = Reservation.builder().start(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0 ,0))).duration(Duration.ofHours(1)).property(property2).user(user).creationTime(Instant.now()).build();
        Reservation reservation5 = Reservation.builder().start(LocalDateTime.of(LocalDate.now(), LocalTime.of(23,  0,0))).duration(Duration.ofHours(1)).property(property2).user(user).creationTime(Instant.now()).build();

        reservationRepository.save(reservation);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);
        reservationRepository.save(reservation4);
        reservationRepository.save(reservation5);

        alreadySetup = true;
    }
}
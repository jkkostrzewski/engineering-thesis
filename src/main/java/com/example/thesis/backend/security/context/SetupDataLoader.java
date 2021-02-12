package com.example.thesis.backend.security.context;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.notice.CommentRepository;
import com.example.thesis.backend.notice.Notice;
import com.example.thesis.backend.notice.NoticeBoard;
import com.example.thesis.backend.notice.NoticeBoardRepository;
import com.example.thesis.backend.notice.NoticeRepository;
import com.example.thesis.backend.reservation.Property;
import com.example.thesis.backend.reservation.PropertyRepository;
import com.example.thesis.backend.reservation.Reservation;
import com.example.thesis.backend.reservation.ReservationRepository;
import com.example.thesis.backend.security.auth.User;
import com.example.thesis.backend.security.auth.UserRepository;
import com.example.thesis.backend.security.utilities.PrivilegeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private final PrivilegeProvider privilegeProvider;
    private final NoticeBoardRepository noticeBoardRepository;
    private final CommentRepository commentRepository;


    @Autowired
    public SetupDataLoader(UserRepository userRepository, NoticeRepository noticeRepository,
                           FloorRepository floorRepository, PropertyRepository propertyRepository,
                           ReservationRepository reservationRepository, PasswordEncoder passwordEncoder,
                           PrivilegeProvider privilegeProvider, NoticeBoardRepository noticeBoardRepository, CommentRepository commentRepository) {
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
        admin.setFirstName("Jan");
        admin.setLastName("Adminowski");
        admin.setEmail("admin@dormapp.com");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setUsername("admin");
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        admin.setEnabled(true);
        admin.setRole(privilegeProvider.admin("admin"));
        admin.setEnabled(true);
        userRepository.save(admin);

        User user = new User();
        user.setFirstName("Krzysztof");
        user.setLastName("Userski");
        user.setEmail("user@dormapp.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setUsername("user");
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setRole(privilegeProvider.user("user"));
        userRepository.save(user);

        User floorAdmin = new User();
        floorAdmin.setFirstName("Zdzisław");
        floorAdmin.setLastName("Piętrowy");
        floorAdmin.setEmail("floorAdmin@dormapp.com");
        floorAdmin.setPassword(passwordEncoder.encode("password"));
        floorAdmin.setUsername("floorAdmin");
        floorAdmin.setAccountNonLocked(true);
        floorAdmin.setAccountNonExpired(true);
        floorAdmin.setCredentialsNonExpired(true);
        floorAdmin.setEnabled(true);
        floorAdmin.setRole(privilegeProvider.floorAdmin("floorAdmin"));
        userRepository.save(floorAdmin);

        File file = new File("src/main/resources/META-INF/resources/images/testImage.jpg");
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file2 = new File("src/main/resources/META-INF/resources/images/testImage2.jpg");
        byte[] bFile2 = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file2);
            fileInputStream.read(bFile2);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String longArticle = "Przyznane dofinansowania mają być przeznaczone na doskonalenie jakości kształcenia i prowadzenie badań. Projekt ma wspierać międzynarodową współpracę szkół doktorskich.\n" +
                "\n" +
                "\n" +
                "PŁ w ramach programu STER zrealizuje projekt „Curriculum for advanced doctoral education & training – CADET Academy of Lodz Univeristy of Technology”. Zakłada on stypendia dla najlepszych doktorantów realizujących prace doktorskie na poziomie międzynarodowym. Ma rozwijać mobilność międzynarodową doktorantów oraz organizację wykładów prowadzonych gościnnie przez światowej klasy specjalistów z różnych dziedzin nauki.\n" +
                "\n" +
                " \n" +
                "Uczelnia planuje, w ramach projektu, opracowanie materiałów promocyjno-informacyjnych na temat kształcenia w Interdyscyplinarnej Szkole Doktorskiej PŁ oraz badań prowadzonych przez zespoły młodych, wybitnych naukowców. Projekt potrawa 3 lata, do końca 2023 roku.\n" +
                "\n" +
                "\n" +
                "Obecnie w Interdyscyplinarnej Szkole Doktorskiej PŁ kształci się 203 doktorantów. W tej grupie jest 26 cudzoziemców z 14 krajów europejskich (Ukraina, Białoruś, Włochy, Rosja, Serbia, Mołdawia) i położonych w innych zakątkach świata (Egipt, Indie, Meksyk, Etiopia, Nigeria, Pakistan, Chiny oraz Iran).";

        String longArticle2 = "- Pierwsza dziesiątka została, tradycyjnie, zdominowana przez uczelnie amerykańskie. Podium należy do University of Washington, Cornell University i Johns Hopkins University. Polska czołówka to Uniwersytet Jagielloński, Uniwersytet Warszawski oraz Akademia Górniczo-Hutnicza. Politechnika Łódzka znacznie awansowała. W poprzedniej edycji było to miejsce 2037. na świecie i 35. w Polsce - wyjaśnia Justyna Kopańska z Centrum Współpracy Międzynarodowej PŁ. - Ranking jest tworzony na podstawie wskaźników bibliometrycznych i webometrycznych, przy czym do tych drugich nawiązuje jego nazwa i te właśnie wskaźniki eksponuje się w opisach i komentarzach tego rankingu najczęściej.\n" +
                "\n" +
                "\n" +
                "Ranking Webometrics to największy ranking światowy, klasyfikowane są w nim wszystkie uczelnie, nie tylko te najlepsze. Uwzględnia się w nim obecność i rozpoznawalność uczelni w sieci. Po raz pierwszy ranking Webometrics został opublikowany w 2004 roku. Jego celem jest zwiększenie obecności instytucji akademickich i badawczych w Internecie i promowanie publikacji w sieci, a także wspieranie wszelkich przedsięwzięć typu Open Access, elektronicznego dostępu do publikacji naukowych i innych materiałów akademickich.\n" +
                "\n" +
                "\n" +
                "Ranking jest publikowany przez Cybermetrics Lab, grupę badawczą hiszpańskiej Najwyższej Rady Badań Naukowych (Consejo Superior de Investigaciones Científicas, CSIC) z siedzibą w Madrycie. Jest to największa publiczna instytucja zajmująca się badaniami naukowymi w Hiszpanii, która podlega Ministerstwu Nauki, Innowacji i Szkolnictwa Wyższego.";


        Notice test_notice_one = Notice.builder().title("PŁ OTRZYMA 2,2 MLN ZŁ NA INTERNACJONALIZACJĘ SZKOŁY DOKTORSKIEJ").body(longArticle).creationDate(Instant.now())
                .image(bFile).createdByUsername("admin").authorFullName(admin.getFullName()).active(true).build();
        Notice test_notice_two = Notice.builder().title("POLITECHNIKA ŁÓDZKA AWANSOWAŁA W RANKINGU WEBOMETRICS").body(longArticle2).creationDate(Instant.now())
                .image(bFile2).createdByUsername("user").authorFullName(user.getFullName()).active(true).build();
        noticeRepository.save(test_notice_one);
        noticeRepository.save(test_notice_two);

        Floor main = Floor.builder().name("Main").build();
        Floor floor = Floor.builder().name("1st Floor").build();
        Floor floor2 = Floor.builder().name("2nd Floor").build();

        user.addFloor(main);
        user.addFloor(floor);

        admin.addFloor(main);
        admin.addFloor(floor);

        floorAdmin.addFloor(main);
        floorAdmin.addFloor(floor);
        floorAdmin.addFloor(floor2);

        NoticeBoard mainBoard = new NoticeBoard("Main Board", main);
        mainBoard.addNotice(test_notice_one);
        mainBoard.addNotice(test_notice_two);

        NoticeBoard floorOneBoard = new NoticeBoard("1st Floor", floor);
        floorOneBoard.addPermissionUser(floorAdmin);

        NoticeBoard floorTwoBoard = new NoticeBoard("2nd Floor", floor2);
        floorTwoBoard.addPermissionUser(floorAdmin);

        noticeBoardRepository.save(mainBoard);
        noticeBoardRepository.save(floorOneBoard);
        noticeBoardRepository.save(floorTwoBoard);


        floorRepository.save(main);
        floorRepository.save(floor);
        floorRepository.save(floor2);

        Property property = Property.builder().name("Laundry Room").owner(floor).build();
        Property property2 = Property.builder().name("Laundry Room").owner(floor2).build();

        propertyRepository.save(property);
        propertyRepository.save(property2);
        propertyRepository.save(Property.builder().name("Laundry Room").owner(floor2).build());

        propertyRepository.save(Property.builder().name("Silownia").owner(main).build());

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
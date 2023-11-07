    package com.sbs.apple.exchange;

    import com.sbs.apple.user.SiteUser;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    @Entity
    public class Exchange {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne
        private SiteUser siteUser;




    }

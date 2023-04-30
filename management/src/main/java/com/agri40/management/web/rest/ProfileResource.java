package com.agri40.management.web.rest;

import com.agri40.management.domain.Profile;
import com.agri40.management.dto.ProfileFilter;
import com.agri40.management.repository.ProfileRepository;
import com.agri40.management.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.agri40.management.domain.Profile}.
 */
@RestController
@RequestMapping("/api")
public class ProfileResource {

    private final Logger log = LoggerFactory.getLogger(ProfileResource.class);

    private static final String ENTITY_NAME = "managementProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ProfileRepository profileRepository;

    public ProfileResource(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * {@code POST  /profiles} : Create a new profile.
     *
     * @param profile the profile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profile, or with status {@code 400 (Bad Request)} if the profile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/profiles")
    public ResponseEntity<Profile> createProfile(@RequestBody Profile profile) throws URISyntaxException {
        log.debug("REST request to save Profile : {}", profile);
        if (profile.getId() != null) {
            throw new BadRequestAlertException("A new profile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Profile result = profileRepository.save(profile);
        return ResponseEntity
            .created(new URI("/api/profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /profiles/:id} : Updates an existing profile.
     *
     * @param id the id of the profile to save.
     * @param profile the profile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profile,
     * or with status {@code 400 (Bad Request)} if the profile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/profiles/{id}")
    public ResponseEntity<Profile> updateProfile(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Profile profile
    ) throws URISyntaxException {
        log.debug("REST request to update Profile : {}, {}", id, profile);
        if (profile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Profile result = profileRepository.save(profile);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profile.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /profiles/:id} : Partial updates given fields of an existing profile, field will ignore if it is null
     *
     * @param id the id of the profile to save.
     * @param profile the profile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profile,
     * or with status {@code 400 (Bad Request)} if the profile is not valid,
     * or with status {@code 404 (Not Found)} if the profile is not found,
     * or with status {@code 500 (Internal Server Error)} if the profile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Profile> partialUpdateProfile(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Profile profile
    ) throws URISyntaxException {
        log.debug("REST request to partial update Profile partially : {}, {}", id, profile);
        if (profile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Profile> result = profileRepository
            .findById(profile.getId())
            .map(existingProfile -> {
                if (profile.getUserId() != null) {
                    existingProfile.setUserId(profile.getUserId());
                }
                if (profile.getDescreption() != null) {
                    existingProfile.setDescreption(profile.getDescreption());
                }
                if (profile.getPhoneNumber() != null) {
                    existingProfile.setPhoneNumber(profile.getPhoneNumber());
                }
                if (profile.getCity() != null) {
                    existingProfile.setCity(profile.getCity());
                }
                if (profile.getProfilePicture() != null) {
                    existingProfile.setProfilePicture(profile.getProfilePicture());
                }
                if (profile.getProfilePictureContentType() != null) {
                    existingProfile.setProfilePictureContentType(profile.getProfilePictureContentType());
                }
                if (profile.getAccountType() != null) {
                    existingProfile.setAccountType(profile.getAccountType());
                }
                if (profile.getRating() != null) {
                    existingProfile.setRating(profile.getRating());
                }
                if (profile.getSmsService() != null) {
                    existingProfile.setSmsService(profile.getSmsService());
                }

                return existingProfile;
            })
            .map(profileRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profile.getId()));
    }

    /**
     * {@code GET  /profiles} : get all the profiles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profiles in body.
     */
    @GetMapping("/profiles")
    public ResponseEntity<List<Profile>> getAllProfiles(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Profiles");
        Page<Profile> page = profileRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /profiles/:id} : get the "id" profile.
     *
     * @param id the id of the profile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/profiles/{id}")
    public ResponseEntity<Profile> getProfile(@PathVariable String id) {
        log.debug("REST request to get Profile : {}", id);
        Optional<Profile> profile = profileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(profile);
    }

    /**
     * {@code DELETE  /profiles/:id} : delete the "id" profile.
     *
     * @param id the id of the profile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/profiles/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String id) {
        log.debug("REST request to delete Profile : {}", id);
        profileRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @PutMapping("/profiles/rate-profile/{id}")
    public ResponseEntity<Profile> updateRating(@PathVariable String id, @RequestBody Integer rating) {
        log.debug("REST request to update Profile : {}", id);
        Optional<Profile> profile = profileRepository.findById(id);
        if (profile.isPresent()) {
            Profile profile1 = profile.get();
            Map<String, Object> map = profile1.getRating();
            if (map == null) {
                map = new HashMap<>();
            }
            rating = rating - 5;
            rating = rating / (Integer) map.get("nbr_review");
            map.put("rating", rating);
            profile1.setRating(map);
            Profile result = profileRepository.save(profile1);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profile1.getId()))
                .body(result);
        }
        return null;
    }

    // get profile info
    @GetMapping("/profiles/profile-data/{id}")
    public ResponseEntity<ProfileFilter> getProfileInfo(@PathVariable String id) {
        log.debug("REST request to get Profile : {}", id);
        // send rabbitmq message to get profile info using user id
        // get profile by id
        Optional<Profile> profile = profileRepository.findByUserId(id);
        if (profile.isPresent()) {
            Profile profile1 = profile.get();
            Map<String, Object> profileEnfo = (Map<String, Object>) rabbitTemplate.convertSendAndReceive(
                "icow.profile",
                profile1.getUserId()
            );
            ProfileFilter profileFilter = new ProfileFilter();
            profileFilter.setCity((String) profileEnfo.get("city"));
            profileFilter.setAccountType((String) profileEnfo.get("accountType"));
            profileFilter.setDescription((String) profileEnfo.get("description"));
            profileFilter.setPhoneNumber((String) profileEnfo.get("phoneNumber"));
            profileFilter.setUserId((String) profileEnfo.get("userId"));
            profileFilter.setProfilePicture((byte[]) profileEnfo.get("profilePicture"));
            profileFilter.setRating((Map<String, Object>) profileEnfo.get("rating"));

            profileFilter.setFirstName((String) profileEnfo.get("firstname"));
            profileFilter.setLastName((String) profileEnfo.get("lastname"));
            profileFilter.setEmail((String) profileEnfo.get("email"));

            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profile1.getId()))
                .body(profileFilter);
        }
        return null;
    }

    @GetMapping("/profiles/filter")
    public ResponseEntity<List<ProfileFilter>> getProfileByCity(@RequestParam String city, @RequestParam String type) {
        log.debug("REST request to get Profile : {}", city + " " + type);
        // get profile by city and type
        List<Profile> profile = profileRepository.findByCityAndAccountType(city, type);
        // create a list of profile filter
        List<ProfileFilter> profileFilters = new ArrayList<>();
        for (Profile profile1 : profile) {
            Map<String, Object> profileEnfo = (Map<String, Object>) rabbitTemplate.convertSendAndReceive(
                "icow.profile",
                profile1.getUserId()
            );
            ProfileFilter profileFilter = new ProfileFilter();
            profileFilter.setCity((String) profileEnfo.get("city"));
            profileFilter.setAccountType((String) profileEnfo.get("accountType"));
            profileFilter.setDescription((String) profileEnfo.get("description"));
            profileFilter.setPhoneNumber((String) profileEnfo.get("phoneNumber"));
            profileFilter.setUserId((String) profileEnfo.get("userId"));
            profileFilter.setProfilePicture((byte[]) profileEnfo.get("profilePicture"));
            profileFilter.setRating((Map<String, Object>) profileEnfo.get("rating"));
            profileFilter.setFirstName((String) profileEnfo.get("firstname"));
            profileFilter.setLastName((String) profileEnfo.get("lastname"));
            profileFilter.setEmail((String) profileEnfo.get("email"));
            profileFilters.add(profileFilter);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, city))
            .body(profileFilters);
    }
    // by account type

}

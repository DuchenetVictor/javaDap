package fr.ynov.dap.dap.data;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author David_tepoche
 *
 */
public interface AppUserRepostory extends CrudRepository<AppUser, Integer> {

    /**
     * .
     *
     * @param userKey dunno
     * @return dunno
     */
    AppUser findByUserKey(String userKey);

}

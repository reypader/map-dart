package com.dart.data.repository;

import com.dart.data.domain.Identity;

/**
 * Interface for complex data access to stored {@link Identity} entities.
 *
 * @author RMPader
 */
public interface IdentityRepository extends CrudRepository<Identity> {

    /**
     * Retrieves an identity that have been issued by a certain provider.
     * The provided identity is not the same as the entity's ID due to
     * possible collision with other providers.
     *
     * @param providedIdentity the supposed identity given by the provider.
     * @param provider         the organization that issued the identity.
     * @return the stored identity for the particular provider
     */
    Identity findIdentityFromProvider(String providedIdentity, String provider);

}

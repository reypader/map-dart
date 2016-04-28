package com.dart.common.service.auth;

import com.dart.common.test.domain.DummyIdentity;
import com.dart.common.test.domain.DummyUser;
import com.dart.data.domain.Identity;
import com.dart.data.factory.IdentityFactory;
import com.dart.data.factory.UserFactory;
import com.dart.data.repository.IdentityRepository;
import com.dart.data.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by rpader on 4/28/16.
 */
public class RegisteringProvidedAuthenticationFilterTest {

    private HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
    private IdentityRepository mockIdentityRepo = mock(IdentityRepository.class);
    private UserRepository mockUserRepo = mock(UserRepository.class);
    private UserFactory mockUserFactory = mock(UserFactory.class);
    private IdentityFactory mockIdentityFactory = mock(IdentityFactory.class);
    private DummyUser dummyUser = new DummyUser();
    private DummyIdentity dummyIdentity = new DummyIdentity();

    @Before
    public void setUp() throws Exception {
        when(mockHttpRequest.getParameter("identity")).thenReturn("id");
        when(mockHttpRequest.getParameter("token")).thenReturn("token");
        when(mockHttpRequest.getParameter("name")).thenReturn("John Doe");
        when(mockHttpRequest.getParameter("email")).thenReturn("dummy@email.com");

        dummyUser.setId("dummy");
        dummyUser.setEmail("dummy@email.com");
        dummyUser.setDisplayName("John Doe");

        dummyIdentity.setId("dummyId");
        dummyIdentity.setProvidedIdentity("id");
        dummyIdentity.setProvider("p");
        dummyIdentity.setUser(dummyUser);

        when(mockUserFactory.createUser(eq("dummy@email.com"), eq("John Doe"))).thenReturn(dummyUser);
        when(mockIdentityFactory.createIdentity(eq(dummyUser), eq("p"), eq("id"))).thenReturn(dummyIdentity);
        when(mockUserRepo.add(eq(dummyUser))).thenReturn(dummyUser);
        when(mockIdentityRepo.add(eq(dummyIdentity))).thenReturn(dummyIdentity);
    }

    @Test
    public void testFetchIdentity() throws Exception {
        when(mockIdentityRepo.findIdentityFromProvider(eq("id"), eq("p"))).thenReturn(dummyIdentity);

        RegisteringProvidedAuthenticationFilter authenticationFilter = new RegisteringProvidedAuthenticationFilter(
                "/test", "p", null, mockIdentityRepo, mockUserRepo, mockUserFactory, mockIdentityFactory);

        Identity identity = authenticationFilter.fetchIdentity(mockHttpRequest, "id", "p");
        verify(mockUserRepo, times(0)).retrieveByEmail(any(String.class));
        verify(mockUserRepo, times(0)).add(any(DummyUser.class));
        verify(mockIdentityRepo).findIdentityFromProvider(eq("id"), eq("p"));
        verify(mockIdentityRepo, times(0)).add(any(DummyIdentity.class));
        assertEquals(dummyIdentity, identity);
    }

    @Test
    public void testFetchIdentityNewUser() throws Exception {
        when(mockUserRepo.retrieveByEmail(eq("dummy@email.com"))).thenReturn(null);
        when(mockIdentityRepo.findIdentityFromProvider(eq("id"), eq("p"))).thenReturn(null);

        RegisteringProvidedAuthenticationFilter authenticationFilter = new RegisteringProvidedAuthenticationFilter(
                "/test", "p", null, mockIdentityRepo, mockUserRepo, mockUserFactory, mockIdentityFactory);

        Identity identity = authenticationFilter.fetchIdentity(mockHttpRequest, "id", "p");
        verify(mockUserRepo).retrieveByEmail(eq("dummy@email.com"));
        verify(mockUserRepo).add(eq(dummyUser));
        verify(mockIdentityRepo).findIdentityFromProvider(eq("id"), eq("p"));
        verify(mockIdentityRepo).add(eq(dummyIdentity));
        assertEquals(dummyIdentity, identity);
    }

    @Test
    public void testFetchIdentityNewIdentity() throws Exception {
        when(mockUserRepo.retrieveByEmail(eq("dummy@email.com"))).thenReturn(dummyUser);
        when(mockIdentityRepo.findIdentityFromProvider(eq("id"), eq("p"))).thenReturn(null);

        RegisteringProvidedAuthenticationFilter authenticationFilter = new RegisteringProvidedAuthenticationFilter(
                "/test", "p", null, mockIdentityRepo, mockUserRepo, mockUserFactory, mockIdentityFactory);

        Identity identity = authenticationFilter.fetchIdentity(mockHttpRequest, "id", "p");
        verify(mockUserRepo).retrieveByEmail(eq("dummy@email.com"));
        verify(mockUserRepo, times(0)).add(any(DummyUser.class));
        verify(mockIdentityRepo).findIdentityFromProvider(eq("id"), eq("p"));
        verify(mockIdentityRepo).add(eq(dummyIdentity));
        assertEquals(dummyIdentity, identity);
    }


}
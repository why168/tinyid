package com.xiaoju.uemc.tinyid.server;

import com.xiaoju.uemc.tinyid.base.generator.IdGenerator;
import com.xiaoju.uemc.tinyid.server.factory.impl.IdGeneratorFactoryServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author du_imba
 */
@SpringBootTest
public class ServerTest {

    @Autowired
    IdGeneratorFactoryServer idGeneratorFactoryServer;

    @Test
    public void testNextId() {
        IdGenerator idGenerator = idGeneratorFactoryServer.getIdGenerator("test");
        Long id = idGenerator.nextId();
        System.out.println("current id is: " + id);
    }
}

package com.blogify.blogapi.conf;

import org.springframework.test.context.DynamicPropertyRegistry;

public class EnvConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add(
        "firebase.private.key",
        () ->
            "{   \"type\": \"service_account\",   \"project_id\": \"fire-login-5fb37\",  "
                + " \"private_key_id\": \"0475d0d3a426247ab922d3a9737c03527e33f101\",  "
                + " \"private_key\": \"-----BEGIN PRIVATE KEY-----\\n"
                + "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDVVguaEc1V74m+\\n"
                + "AJB2ZE2fAi9sDt3P+QifYRgyqlxYantyZ46o2aGcjPjVAmcAIHEcuDNA0PIxvF2r\\n"
                + "0aaUCGrf9aZoifoth6shwZVzXpT1qZYjbO9LwKRtHuW7aMP7SBLeKT5oISsxgPI2\\n"
                + "rVETrNtpbLe1/xDoE9Avu9DripriKmk78Xp38jY7+V86njvGATNlhxFSLzWVgc2L\\n"
                + "/ldOHfOADC7kdtgy34wSmNvSMBChx1xoytPVrvX1tlU6QZgULtwbvVOgIS1c9cTG\\n"
                + "V73s4HmkIo0p77+emy3uxo6TsoFxm2tKiOyQNiGkHJiBzlncE/azPqsLR+C8IzUl\\n"
                + "C1hg2J63AgMBAAECggEABBvxLe7eg/jmL/kLefb6oRpwrBB+k0KaY5TXhiT5PGFo\\n"
                + "rqPHVVdDy/knKICFrV9H7nlZ0LYgRCGlsuk0ioYS+5dcCjLGKoRXdoH9DUjVblnt\\n"
                + "i2cja35ywatRBNLCxkn1cGleS1vVh3w9WMeZLTDXf74EAocUM0mBFA+hDuMPkqry\\n"
                + "H8snLPWrt2LOX8QAqlNAkTmT8QuKm0oOGQWa7suxGfY+Orf1wuOyP9tglKu9QO3U\\n"
                + "ya0rUa2Mxtl/UC/RNjUTgYnld/rOibK6R6hzPDmqOe42mEFMAzV4m0409oFnxGM1\\n"
                + "ArMdU/pPPqrhg5ZbPB347xoR4AJfpcUmOsT9VRs3MQKBgQDsJyFoTIYpG2iUB0br\\n"
                + "z5CopaK/CvNLvXBvjXDX5jKAqe3ZJk95AwMVX/2VksVjdQSnjazbopMa3MaPkoUO\\n"
                + "zT87RcPJcY47hV6swT8N4o9V4J+UMOBWAq/akAGsHNtReF+PEnqjwxcFoyTVknwe\\n"
                + "qv04XGIoIsKhNhHbUJ7nY1lB3wKBgQDnRAI8dtOuAMFhBUwA30UHk7hHvddUAoV+\\n"
                + "Fq2vL2o4Bv2lP27/bcHeaylqVhC3cx1Y894FaZ7oMsWOjtVVBH6o5AWA4NiaRAiq\\n"
                + "biIsWraVNd5lYBqlvOCdXpql4rj7jueeLm5nz4+eI0hwTPkacfFIbi63nrgEeuMu\\n"
                + "LUXSPykuKQKBgDbmkfmxcK811AY1+uLvim3qs/8+CHnfPLhyZX2L4snOt6D4Kxmy\\n"
                + "/ZCGlGmliYYpuj9JzJnzHcfs86yFH7HlPYIGIhI++BYWV4BKHOKZfUE1y2OkSUM1\\n"
                + "tLRXLkxZGRPDrFOStDhGLp/84Z9VQs3Oargjoh46lozsCRK3l3mruyB5AoGAUGHQ\\n"
                + "ESoQG0vB6zIHH8p62M5cX/s5K8cQlSnWxqxJVB4B7Xzl3LxOhTtkEsTFXK4TRkbF\\n"
                + "JcZ3aCe1iP5WYg2E5LkUvJ3qS3xYMrzC/f2ttqEZoszLlk3XGHu4bu4l87HRCacD\\n"
                + "0utO9N04oWfvsWRnRBCgBkyRk2UPL46m1RQFATkCgYEAyOmeznfdYa7xuTWxxWAb\\n"
                + "kcxUVHkrLk6ATyetDNT41Up8UGgHW8Eyrw9QJp8R1FuIS9TAm3BC3F0pzBnP4PoQ\\n"
                + "2Yexzm8TPD1C7RaAsE6shXH79UcOU+p8z1ROTPb0vdOf4K77fA//nRvOOL96Fj+P\\n"
                + "16PQOR+MtwNIjSBEj43vvB4=\\n"
                + "-----END PRIVATE KEY-----\\n"
                + "\",   \"client_email\":"
                + " \"firebase-adminsdk-q1bq5@fire-login-5fb37.iam.gserviceaccount.com\",  "
                + " \"client_id\": \"102809297624984073416\",   \"auth_uri\":"
                + " \"https://accounts.google.com/o/oauth2/auth\",   \"token_uri\":"
                + " \"https://oauth2.googleapis.com/token\",   \"auth_provider_x509_cert_url\":"
                + " \"https://www.googleapis.com/oauth2/v1/certs\",   \"client_x509_cert_url\":"
                + " \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-q1bq5%40fire-login-5fb37.iam.gserviceaccount.com\","
                + "   \"universe_domain\": \"googleapis.com\" }");
  }
}

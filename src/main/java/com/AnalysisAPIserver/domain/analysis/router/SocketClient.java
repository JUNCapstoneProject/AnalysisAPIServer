package com.AnalysisAPIserver.domain.analysis.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * SocketClient는 소켓 통신을 위한 클라이언트 클래스입니다.
 * 주어진 호스트와 포트로 메시지를 전송하고 응답을 받습니다.
 * 이 클래스는 확장을 고려하지 않았으므로 final로 선언합니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public final class SocketClient {

    /**
     * JSON 직렬화 및 역직렬화를 위한 ObjectMapper 입니다.
     */
    private final ObjectMapper objectMapper;

    /**
     * 주어진 호스트와 포트로 메시지를 전송하고 응답 문자열을 받습니다.
     * 이 메소드는 확장을 고려하여 설계되지 않았습니다.
     *
     * @param host 대상 서버의 호스트 주소입니다.
     * @param port 대상 서버의 포트 번호입니다.
     * @param messageObject 전송할 메시지 객체입니다.
     * @return 서버로부터 받은 응답 문자열입니다. 통신 실패 시 null을 반환합니다.
     */
    public String sendMessage(final String host,
                              final int port,
                              final Object messageObject) {
        try (Socket socket = new Socket(host, port)) {
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();


            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));

            String jsonMessage = objectMapper.writeValueAsString(messageObject);
            writer.write(jsonMessage);
            writer.newLine();
            writer.flush();

            return reader.readLine();

        } catch (IOException e) {
            log.error("Socket communication failed", e);
            return null;
        }
    }
}

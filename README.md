# Next Step

# chapter03 was

# chapter04 Refactoring

## RequestHandler Refactoring

모든 로직이 RequestHandler를 통해서 동작하고 있다. 객체가 서로 협력할 수 있게끔 리팩토링을 진행.

협력 책임 역할

- 객체를 협력하게 하기 위해 자율적인 객체를 만들기 위해 내부 구현을 캡슐화한다
- 

### HTTP Request

 브라우저로부터 받은 요청을 읽어서 처리하기 위해는 어떤 객체의 협력을 통해 이루어질까
먼저 브라우저의 요청을 읽는 객체가 있을 것이다. 이 객체가 브라우저의 요청을 읽으면
브라우저의 어떤 요청인지 읽어보아야 한다. 

 Request Line, Request Header, Request body가 있을 것이다. GET method를 통해 브라우저가 데이터를 요청했다면 Request body의 값은 없을 것이다.
 그러면 Request Line, Header, Body를 파싱하는 객체가 있어야할 것 같다. 파싱을 통해 브라우저가 원하는 게 뭔지 알 수 있을 것이다.

 데이터 중심 관점이 아닌 책임 중심 관점으로 리팩토링을 해보자.
 HTTP Request 에 관한 책임을 수행하기 위해서는
 - 브라우저로부터 요청을 읽어오는 객체
 - 요청을 읽어 Request Line, header, body로 각각 파싱하는 객체 -> 파싱이라는 게 결국 읽는 것
   - 파싱하는 객체는 브라우저의 요청을 응답하기 위해 파싱한 데이터를 Response 책임을 가진 객체에게 응답하라라고 메시지를 던진다

### HTTP Request 고민한 내용

RequestReader 객체에서 쿠키까지 파싱해서 HttpRequest 객체에게 주는 게 맞을까하는 생각이 들었다
-> header를 읽어서 HttpRequest 객체에게 주고 있기 때문에 HttpRequest 객체가 쿠키를 파싱하는 책임을 갖도록 변경

### HTTP Response

RequestReader에서 브라우저의 요청을 읽고 HttpRequest에게 Request Line, Header, Body 를 파싱해서 주었다.
HttpRequest가 이제 HTTP method, url, body 등 데이터를 통해 브라우저에게 응답할 데이터를 만들어라고 객체에게 메시지를 줄 차례다.
브라우저에게 Response 해주기 위해 어떤 요청이 왔는지에 따라 처리하기 위해 ResponseWriter 객체를 두어 응답

오 여기서 인터페이스 사용을 해도 되겠다. HTTP method에 따라 GET, POST 

브라우저로 응답하기 위해 ResponseWriter가 request method, url, header, body 데이터를 통해 어떤 요청이 온 건지 확인한다.
확인한 걸 통해 요청 받은 걸 응답해주기 위해 또 하나의 객체에게 응답하도록 메시지를 던진다.
메시지를 받은 그 객체는 GET 요청이면 원하는 html 파일을 읽어서 응답하기 위해 하나의 객체에게 메시지를 던지고
POST 요청이면 body 데이터를 샬라샬라 이 또한 하나의 객체가 처리한다ㅏㅏㅏ

HttpRequest 객체가 가지고 있는 method, url, header, body로
브라우저에서 요청한 것을 수행하고 HTTP Status code 와 같은 걸 response 에게 응답하도록 메시지를 던진다

갑자기 데이터에 갇히게 되니 진행이 어려워진다


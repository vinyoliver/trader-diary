<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <%--<link rel="shortcut icon" href="img/favicon.ico" media="screen">--%>
    <title>Login | Shark Diary </title>
	<!-- Bootstrap core CSS -->
	<link href="styles/bootstrap.min.css" rel="stylesheet">
	<link href="styles/login.css" rel="stylesheet">
	<script src="scripts/vendor/jquery-2.1.4.min.js"></script>
	<script src="scripts/vendor/bootstrap.min.js"></script>
	<script src="scripts/vendor/jquery.maskedinput.min.js"></script>
	<script src="scripts/vendor/modernizr-2.8.3.min.js"></script>

	<script>
		jQuery(function ($) {
//			$(".cpf").mask("999.999.999-99");

			$(document).ready(function(){
				var altura_documento = $(document).height();
				var altura_tela = $(window).height();
				var largura_tela = $(window).width();
			
				if (altura_tela < 550) {
					$(".brand-login img").css("margin-top", 0);
				} else {
					$(".brand-login img").css("margin-top", altura_tela * 0.50 - 280);
					$("footer").css("margin-top", 50);
				};
				  
				$( window ).resize(function() {
					var altura_documento = $(document).height();
				 	var altura_tela = $(window).height();
				 	
					if (altura_tela < 550) {
						$(".brand-login img").css("margin-top", 0);
					} else {
						$(".brand-login img").css("margin-top", altura_tela * 0.50 - 280);
						$("footer").css("margin-top", 50);

					}; 
				 
				});

			});

			var deviceAgent = navigator.userAgent.toLowerCase();

			var isTouchDevice = Modernizr.touch || 
			(deviceAgent.match(/(iphone|ipod|ipad)/) ||
			deviceAgent.match(/(android)/)  || 
			deviceAgent.match(/(iemobile)/) || 
			deviceAgent.match(/iphone/i) || 
			deviceAgent.match(/ipad/i) || 
			deviceAgent.match(/ipod/i) || 
			deviceAgent.match(/blackberry/i) || 
			deviceAgent.match(/bada/i));

			if (isTouchDevice) {
                var images = ['bg_login_1_mobile.jpg'];
				$('body').css({'background-image': 'url(img/' + images[Math.floor(Math.random() * images.length)] + ')'});			
		    } else {
				var images = ['bg_login_01.jpg'];
				$('body').css({'background-image': 'url(img/' + images[Math.floor(Math.random() * images.length)] + ')'});
		    }			
		});
	</script>
</head>

<body>
	<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
		<div class="row">
			<div class="alert alert-danger alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
						aria-hidden="true">&times;</span></button>
				<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
				Login e/ou senha informados s&atilde;o inv&aacute;lidos.
			</div>
		</div>
	</c:if>
	<div class="container">
		<div class="row">
			<div class="col-xs-12 col-md-4"></div>
			<div class="col-xs-12 col-md-4 text-center">
				<div class="brand-login">
					<img src="/img/logomarca.png"
                         alt="Logo Trader Diary"
                         class="img-responsive center-block" style="visibility: hidden"/>
				</div>
			</div>
			<div class="col-xs-12 col-md-4"></div>
	
		</div>
		<div class="row">
			<div class="col-xs-1 col-md-4"></div>
			<div class="col-xs-10 col-md-4 login-painel">
				<form role="form" role="form" method="post" action="/j_spring_security_check">
					<div class="form-group">
						<label for="username">Email</label>
						<input type="text" name="username" id="username" class="form-control cpf" placeholder="Email" required
							   autofocus>
					</div>
					<div class="form-group">
						<label for="password">Senha</label>
						<input type="password" name="password" id="password" class="form-control" placeholder="Senha" required
							   maxlength="10">
					</div>
					<div class="text-center">
						<button class="btn btn-acao" type="submit"><span class="glyphicon glyphicon-lock"></span>&nbsp;Entrar</button>
					</div>
					<hr></hr>
					<div class="col-xs-12 col-md-12 text-center">Esqueceu a senha?&nbsp;<a data-toggle="collapse" data-target="#informacoes">Clique aqui</a></div>
					<div id="informacoes" class="collapse text-center col-xs-12 col-md-12">
                        <div class="small">Envie um email para:</div>
                        <div class="lead"><span class="glyphicon glyphicon-envelope"></span>&nbsp;vinyoliver.ti@gmail.com
                        </div>
					</div>
				</form>
			</div>
			<div class="col-xs-1 col-md-4"></div>
		</div>
	</div>
	<footer class="text-center">
		<p style="color: #ffffff; font-weight: bold">&copy; Copyright 2016. Todos os direitos reservados.</p>
	</footer>
</body>
</html>
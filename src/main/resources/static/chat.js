$(function() {
    'use strict';

    var client;

    function showMessage(mesg) {
		$('#messages').append('<tr>' +
		  '<td>' + mesg.from + '</td>' +
		  '<td>' + mesg.to + '</td>' +
		  '<td>' + mesg.text + '</td>' +
		  '<td>' + mesg.time + '</td>' +
		  '</tr>');
	}

    function getSession(stompClient) {
		var url = stompClient.ws._transport.url;
		url = url.replace("ws://localhost:9090/chat/",  "");
		url = url.replace("/websocket", "");
		url = url.replace(/^[0-9]+\//, "");
		return url;
	}
    
    function setConnected(connected) {
		$("#connect").prop("disabled", connected);
		$("#disconnect").prop("disabled", !connected);
		$('#text').prop('disabled', !connected);
		if (connected) {
			$("#conversation").show();
			$('#text').focus();
		}
		else $("#conversation").hide();
		
		$("#messages").html("");
    }

    $("form").on('submit', function (e) {
		e.preventDefault();
    });

    $('#disconnect, #text').prop('disabled', true);

	$('#connect').click(function() {
		if ($('#username').val()) {
			client = Stomp.over(new SockJS('http://localhost:9090/chat'));
			client.connect({}, function (frame) {
				var session = getSession(client);
				$.get('http://localhost:9090/register?username=' + $('#username').val() + '&sessionId=' + session, function () {
					setConnected(true);
					client.subscribe('/conversation/user-' + session, function (message) {
						showMessage(JSON.parse(message.body));
					});
					var rooms = ['room1', 'room2', 'room3'];
					rooms.forEach(function (value) {
						client.subscribe('/room/' + value, function (message) {
							showMessage(JSON.parse(message.body));
						});
					});
				});
			});
		}
	});

    $('#disconnect').click(function() {
		if (client != null) {
			client.disconnect();
			setConnected(false);
		}
		client = null;
    });

    $('#send').click(function() {
		if ($('#to').val()) {
			client.send("/app/chat/user", {}, JSON.stringify({
				to: $('#to').val(),
				text: $('#text').val(),
			}));
			$('#text').val("");
		}
    });

	$('#sendRoom').click(function() {
		client.send("/app/room/send/" + $('#roomId').val(), {}, JSON.stringify({
			text: $('#text').val(),
		}));
		$('#text').val("");
	});
});

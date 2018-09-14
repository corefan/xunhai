$(document).ready(function() {
	$(".menu > a").click(function() {
		var ulNode = $(this).next("ul");
		ulNode.slideToggle("fast");
	});
});
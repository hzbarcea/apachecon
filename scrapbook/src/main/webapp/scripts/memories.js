$(function() {

	// initialize scrollable
	$("#container").scrollable();
	
	// initialize fancybox
	$("a.fancy").fancybox({'type': 'image'});


    $('.browsable .item').mouseover(function() {
        $(this).children('.editor').css('display', 'block');
    });
    $('.browsable .item').mouseout(function() {
        $(this).children('.editor').css('display', 'none');
    });
});

/* not used
function uploadImages(){

	//get the input and UL list
	var input = document.getElementById('filesToUpload');	
	
	//for every file...
	for (var n = 0; n < input.files.length; n++) {
		
		//add to list	  
		var html = '<div class="row" id="img_' + n + '">';
		html += '<div class="icon pending"></div>';
		html += '<div class="img-file">' + input.files[n].name + '</div>';
		html += '</div>';
			  
		$("#file-list").append(html);
		
		//Ajax upload goes here
		
		//fake ajax status. To be replaced by real ajax
		var loadingTime = (Math.random()*10 + 1);
		
		//fake pending
		setTimeout('fakeAjax('+ n +',' + loadingTime + ')', loadingTime*1000);
	
	}
}

function fakeAjax(i, loadingTime){	
		
	if (loadingTime < 9){
		//fake upload success
		$('#img_' + i + ' .icon').removeClass('pending').addClass('success');
		$('#img_' + i).append('<a class="cancel" href="javascript:void(0);" onclick="removeUploaded(' + i + ');">Remove</a>');
	} else {
		//fake upload fail
		$('#img_' + i + ' .icon').removeClass('pending').addClass('failed');
	}
}

function removeUploaded(i){
	$('#img_' + i).remove();
	
	//remove from db with ajax
}

function declineImage(el){
	$(el).parents(".item").unbind().append("<div class='declined'></div>");
	$(el).parents(".editor").remove();	
	
	// Do some ajax here to decline image on server
}

function approveImage(el){
	$(el).parents(".item").unbind().append("<div class='approved'></div>");
	$(el).parents(".editor").remove();
	
	// Do some ajax here to approve image on server
}
//*/
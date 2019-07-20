$(document).ready(function(){
        var HeaderTop = $('.menu_block').offset().top;
        $(window).scroll(function () {
            if ($(window).scrollTop() > HeaderTop) {
                $('.menu_block').css({
                    position: 'fixed',
                    top: '0px'
                });
                $('.menu_block').addClass('active');
                $('.logo_main img').attr('src', './img/logo_bile.svg');
            } else {
                $('.menu_block').css({
                    position: 'absolute'
                });
                $('.menu_block').removeClass('active');
                $('.logo_main img').attr('src', './img/logo.svg');
            }
        });

	$('.back').click(function(){
	    $('.sub-links .list ul').removeClass('active');
	    $('.sub-links').removeClass('mobile-opened');
	})
	
	
        $('.slide_content').slick({
            dots: false,
            infinite: false,
            speed: 300,
            arrows: true,
            slidesToShow: 4,
            slidesToScroll: 4,
            
            responsive: [{
                    breakpoint: 1024,
                    settings: {
                        slidesToShow: 3,
                        slidesToScroll: 3,
                        infinite: true,
                    }
                },
                {
                    breakpoint: 600,
                    settings: {
                        slidesToShow: 2,
                        slidesToScroll: 2
                    }
                },
                {
                    breakpoint: 480,
                    settings: {
                        slidesToShow: 1,
                        slidesToScroll: 1
                    }
                }
                // You can unslick at a given breakpoint now by adding:
                // settings: "unslick"
                // instead of a settings object
            ]
        });
        
        
	  /* ----------------------------------------------------------------
	    zopim
	  ---------------------------------------------------------------- */  
	  $zopim(function() {  
	    $zopim.livechat.bubble.hide();
	    $zopim.livechat.window.hide();
	    $zopim.livechat.setLanguage('de');
	  });
});


window.$zopim||(function(d,s){var z=$zopim=function(c){z._.push(c)},$=z.s= d.createElement(s),e=d.getElementsByTagName(s)[0];z.set=function(o){z.set. _.push(o)};z._=[];z.set._=[];$.async=!0;$.setAttribute('charset','utf-8'); $.src='https://cdn.zopim.com/?TPnpBjKsMdlB3DTLgqeXbp0gLqzuOiLw';z.t=+new Date;$. type='text/javascript';e.parentNode.insertBefore($,e)})(document,'script');


function openMenu(){
    var displayMenu = $('.fixed-nav').css('display');
    if(displayMenu === 'none'){
        $('.fixed-nav').show();
        $('.menu_block').removeClass('active');
        $('.menu_block').addClass('mobail_head');
        $('.menu_block').addClass('page_head_element');
        $('.menu_show_btn .icon').addClass('active');
    }else{
        if($(window).scrollTop() > 120){
            $('.menu_block').addClass('active');
        }
        $('.fixed-nav').hide();
        $('.menu_block').removeClass('page_head_element');
        $('.menu_show_btn .icon').removeClass('active');
        $('.menu_block').removeClass('mobail_head');
    }

}

function openSubMenu(th){
    var blockOpen = $(th).attr('data-val');
    console.log(this);
    $('.sub-links').addClass('mobile-opened');
    $('.sub-links .list ul').removeClass('active');
    $('.sub-links .'+blockOpen).addClass('active');
}

function photoMenu(idM){
    $('.sub-links').addClass('mobile-opened');
    $('.sub-links .list ul').removeClass('active');
    $('.sub-links .'+idM).addClass('active');
}

function slideFaq(id){
    var contentVisible = $('#faq_content_'+id).css('display');

    if(contentVisible === "none"){
        $('#faq_content_'+id).slideToggle(500);
        $('#faq_title_'+id).addClass('active');
        $('#faq_title_'+id+'.active .icon_faq i').removeClass('fa-plus');
        $('#faq_title_'+id+'.active .icon_faq i').addClass('fa-minus');
    }else{
        $('#faq_content_'+id).slideToggle(500);
        $('#faq_title_'+id).removeClass('active');
        $('#faq_title_'+id+' .icon_faq i').removeClass('fa-minus');
                $('#faq_title_'+id+' .icon_faq i').addClass('fa-plus');
    }
}

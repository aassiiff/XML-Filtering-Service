function removeClass(menu) {
	//alert("remove Class called: " + menu);
	//jQuery('#menu li').removeClass('active');
	jQuery(menu).parent().children().removeClass('active');
	jQuery(menu).addClass('active');
}
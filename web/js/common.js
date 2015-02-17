function changeToURL(url_)
{
    window.location.href = url_;
}


function submitForm(formName, actionURL)
{
	try
	{
		var form = document.getElementById(formName);
		form.action = actionURL;
		form.submit();
	}
	catch(e)
	{
		return;
	}
}


function submitFormInNewWindow(formName, actionURL)
{
	try
	{
		var form = document.getElementById(formName);
		form.action = actionURL;
		form.target = "_blank";
		form.submit();
	}
	catch(e)
	{
		return;
	}
}


var formatBoolean = function(field,index,cell)
{
	if (field == null)
	{
		return '';
	}
	if (field)
	{
    	return 'YES';
	}
	else
	{
		return 'NO';
	}
}
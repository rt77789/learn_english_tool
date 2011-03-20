#!/usr/bin/ruby
#
#
#
# A program for auto login 3g.renren.com, and auto update status / write new diary too.
#
# author: xiaoe
# email: xiaoe@mail.ustc.edu.cn

require 'rubygems'
require 'mechanize'

class RenRen
	def initialize
		@agent = Mechanize.new { |agent|
			agent.user_agent_alias = 'Mac Safari'
		}
		# renren mobile 入口
		@login_url = 'http://3g.renren.com/login.do?autoLogin=true'
		# 控制调试选项
		@debug = false 
	end

	# login to the 3g.renren.com
	# usage: rr.login('email', 'password')
	def login(email, password)
		@agent.get(@login_url) do |page|
			form = page.forms.first # => Mechanize::Form

			form.fields.each { |f| puts f.name; puts f.value } if @debug

			form['email'] = email
			form['password'] = password
			@page = @agent.submit(form)
		end
		self
	end

	# update a new status.
	# usage: rr.update_status '但愿人长久，千里共婵娟'
	def update_status msg
		form = @page.forms.first
		form.fields.each { |f| puts f.name; puts f.value } if @debug
		form['status'] = msg
		@page = @agent.submit(form)
		self
	end

	# write a diary
	# usage: rr.write_diary('但愿人长久，千里共婵娟', '但愿人长久，千里共婵娟', '123456').body
	def write_diary(title, content, password)
		@page = @page.links.find { |l| l.text == '写日志' }.click

		form = @page.forms.first

		form['title'] = title
		form['body'] = content
		form['passwd'] = password
		form.fields.each { |f| puts f.name; puts f.value } if @debug

		@page = @agent.submit(form)
		self
	end
end

if $0 == __FILE__ 
	rr = RenRen.new
	rr.login('hellow00d@yeah.net', 'hellow00d')
	rr.update_status('其实我想走');
	rr.write_diary('其实我想走', '其实我想走', '')
end

#!/usr/bin/ruby


require 'singleton'
# Morpher: plural to singular.

class Morpher
	include Singleton
	# 
	def __init__
		@@irregular_map = Hash.new
		@@irregular_map["a"] = "a"
	end

	# 
	def dup 
		@@irregular_map.each do |k,v|
			puts "#{k}, #{v}"
		end
	end

end

Morpher.instance().__init__

if __FILE__ == $0
	Morpher.instance().dup
end

#!/usr/bin/ruby

#
#
require 'rubygems'
require 'nokogiri'
require 'open-uri'
require 'nlp/tokenizer'
require 'set'

class Page
    # url is the target page's url.
    def initialize(url)
        @url = url
		@tokens = Hash.new
		@stopwords = Set.new
		load_stopwords('data/stopwords')
		# load morpher data files.
    end

	# load stopwords file.
	def load_stopwords(file_name)
		File.open(file_name, "r").each do |line|
			@stopwords.add(line.chomp)
		end
	end

    # download the target page.
    def download
        @doc = Nokogiri::HTML(open(@url))
	end

    # extract content.
    def extract_content
		# define the extract format.
        #doc.css('div[class = "code"] > pre[class = "ruby"]').each do |node|
        @doc.xpath('//div[@id="main"]').each do |node|
            Tokenizer.segment(node.text).each do |token|
				if @tokens[token].nil?
					@tokens[token] = 1
				else
					@tokens[token] += 1
				end
            end
        end
    end

	# Plural to Singular
    def singular
		@tokens.each_key do |token|
			# use morpher to get singular.
		end
    end

    # remove stopwords
    def remove_stopwords
		@tokens.each_key do |k|
			@tokens.delete(k) if @stopwords.include?(k)
		end
    end

    # predict word
    def predict_word
    end

    # print the result.
    def print_result
		@tokens.sort { |a,b| b[1] <=> a[1] }.each do |k,v|
			puts "#{k}\t#{v}"
		end
    end
end

if __FILE__ == $0 
	#url = 'http://www.techotopia.com/index.php/Ruby_Essentials'
	url = 'http://home.ustc.edu.cn/~xiaoe/posts/Online%20music/'
    page = Page.new(url)
	page.download
	page.extract_content
	page.singular
	page.remove_stopwords
	page.print_result
end

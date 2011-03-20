#!/usr/bin/ruby

#
#
require 'rubygems'
require 'nokogiri'
require 'open-uri'
require 'nlp/tokenizer'

class Page
    # url is the target page's url.
    def initialize(url)
        @url = url
    end

    # download the target page.
    def download
        doc = Nokogiri::HTML(open(@url))
        # define the extract format.
        doc.css('div[class = "code"] > pre[class = "ruby"]').each do |node|
            tokens = Tokenizer.segment(node.text)
            tokens.each do |token|
                puts token
            end
        end
    end

    # extract content.
    def extract_content
    end

    # English segment with nlp/
    def segment
    end

    # Plural to Singular
    def singular
    end

    # remove stopwords
    def remove_stopwords
    end

    # predict word
    def predict_word
    end

    # print the result.
    def print_result
    end
end

if __FILE__ == $0 
    url = 'http://hi.baidu.com/zcl369369/blog/item/3908368f427ceaeb513d92cb.html'
    Page.new(url).download
end

class ForumThread < ActiveRecord::Base
	validates :title, :presence => true
	validates :initial_post, :presence => true
	
	belongs_to :user
	has_many :posts
end

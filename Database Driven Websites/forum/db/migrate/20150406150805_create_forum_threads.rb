class CreateForumThreads < ActiveRecord::Migration
  def change
    create_table :forum_threads do |t|
      t.string :title
      t.integer :post_count

      t.timestamps null: false
    end
  end
end

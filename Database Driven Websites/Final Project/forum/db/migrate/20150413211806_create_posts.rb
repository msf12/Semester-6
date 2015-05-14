class CreatePosts < ActiveRecord::Migration
  def change
    create_table :posts do |t|
      t.string :contents
      t.references :user, index: true, foreign_key: true
      t.references :forum_thread, index: true, foreign_key: true

      t.timestamps null: false
    end
  end
end

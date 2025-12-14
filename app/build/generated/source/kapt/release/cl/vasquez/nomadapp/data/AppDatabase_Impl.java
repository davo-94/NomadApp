package cl.vasquez.nomadapp.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile PostDao _postDao;

  private volatile ContactDao _contactDao;

  private volatile UserDao _userDao;

  private volatile AdminUserDao _adminUserDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(5) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `posts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `date` TEXT NOT NULL, `imageUris` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `contact_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `correo` TEXT NOT NULL, `pais` TEXT NOT NULL, `mensaje` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `email` TEXT NOT NULL, `password` TEXT NOT NULL, `role` TEXT NOT NULL, `username` TEXT NOT NULL, `firstName` TEXT NOT NULL, `lastName` TEXT NOT NULL, `enabled` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `admin_users` (`id` INTEGER NOT NULL, `username` TEXT NOT NULL, `email` TEXT NOT NULL, `firstName` TEXT NOT NULL, `lastName` TEXT NOT NULL, `roles` TEXT NOT NULL, `enabled` INTEGER NOT NULL, `syncPending` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2192704f1ba91e1c4e78fb595b0a5a02')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `posts`");
        db.execSQL("DROP TABLE IF EXISTS `contact_table`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `admin_users`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPosts = new HashMap<String, TableInfo.Column>(5);
        _columnsPosts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosts.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosts.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosts.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosts.put("imageUris", new TableInfo.Column("imageUris", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPosts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPosts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPosts = new TableInfo("posts", _columnsPosts, _foreignKeysPosts, _indicesPosts);
        final TableInfo _existingPosts = TableInfo.read(db, "posts");
        if (!_infoPosts.equals(_existingPosts)) {
          return new RoomOpenHelper.ValidationResult(false, "posts(cl.vasquez.nomadapp.data.Post).\n"
                  + " Expected:\n" + _infoPosts + "\n"
                  + " Found:\n" + _existingPosts);
        }
        final HashMap<String, TableInfo.Column> _columnsContactTable = new HashMap<String, TableInfo.Column>(5);
        _columnsContactTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContactTable.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContactTable.put("correo", new TableInfo.Column("correo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContactTable.put("pais", new TableInfo.Column("pais", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContactTable.put("mensaje", new TableInfo.Column("mensaje", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysContactTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesContactTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoContactTable = new TableInfo("contact_table", _columnsContactTable, _foreignKeysContactTable, _indicesContactTable);
        final TableInfo _existingContactTable = TableInfo.read(db, "contact_table");
        if (!_infoContactTable.equals(_existingContactTable)) {
          return new RoomOpenHelper.ValidationResult(false, "contact_table(cl.vasquez.nomadapp.data.Contact).\n"
                  + " Expected:\n" + _infoContactTable + "\n"
                  + " Found:\n" + _existingContactTable);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(8);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("password", new TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("firstName", new TableInfo.Column("firstName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("lastName", new TableInfo.Column("lastName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(cl.vasquez.nomadapp.data.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsAdminUsers = new HashMap<String, TableInfo.Column>(8);
        _columnsAdminUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdminUsers.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdminUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdminUsers.put("firstName", new TableInfo.Column("firstName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdminUsers.put("lastName", new TableInfo.Column("lastName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdminUsers.put("roles", new TableInfo.Column("roles", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdminUsers.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdminUsers.put("syncPending", new TableInfo.Column("syncPending", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAdminUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAdminUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAdminUsers = new TableInfo("admin_users", _columnsAdminUsers, _foreignKeysAdminUsers, _indicesAdminUsers);
        final TableInfo _existingAdminUsers = TableInfo.read(db, "admin_users");
        if (!_infoAdminUsers.equals(_existingAdminUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "admin_users(cl.vasquez.nomadapp.data.AdminUser).\n"
                  + " Expected:\n" + _infoAdminUsers + "\n"
                  + " Found:\n" + _existingAdminUsers);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2192704f1ba91e1c4e78fb595b0a5a02", "4010858b8315af00838efa6e532578c1");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "posts","contact_table","users","admin_users");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `posts`");
      _db.execSQL("DELETE FROM `contact_table`");
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `admin_users`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PostDao.class, PostDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ContactDao.class, ContactDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AdminUserDao.class, AdminUserDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PostDao postDao() {
    if (_postDao != null) {
      return _postDao;
    } else {
      synchronized(this) {
        if(_postDao == null) {
          _postDao = new PostDao_Impl(this);
        }
        return _postDao;
      }
    }
  }

  @Override
  public ContactDao contactDao() {
    if (_contactDao != null) {
      return _contactDao;
    } else {
      synchronized(this) {
        if(_contactDao == null) {
          _contactDao = new ContactDao_Impl(this);
        }
        return _contactDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public AdminUserDao adminUserDao() {
    if (_adminUserDao != null) {
      return _adminUserDao;
    } else {
      synchronized(this) {
        if(_adminUserDao == null) {
          _adminUserDao = new AdminUserDao_Impl(this);
        }
        return _adminUserDao;
      }
    }
  }
}

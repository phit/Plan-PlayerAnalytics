package com.djrapitops.plan.command.commands.manage;

import com.djrapitops.plan.api.exceptions.database.DBInitException;
import com.djrapitops.plan.system.database.DBSystem;
import com.djrapitops.plan.system.database.databases.Database;
import com.djrapitops.plan.system.settings.Permissions;
import com.djrapitops.plan.system.settings.locale.Locale;
import com.djrapitops.plan.system.settings.locale.Msg;
import com.djrapitops.plan.utilities.Condition;
import com.djrapitops.plan.utilities.ManageUtils;
import com.djrapitops.plugin.api.utility.log.Log;
import com.djrapitops.plugin.command.CommandNode;
import com.djrapitops.plugin.command.CommandType;
import com.djrapitops.plugin.command.ISender;
import com.djrapitops.plugin.task.AbsRunnable;
import com.djrapitops.plugin.task.RunnableFactory;
import com.djrapitops.plugin.utilities.Verify;

/**
 * This manage subcommand is used to backup a database to a .db file.
 *
 * @author Rsl1122
 * @since 2.3.0
 */
public class ManageBackupCommand extends CommandNode {

    public ManageBackupCommand() {
        super("backup", Permissions.MANAGE.getPermission(), CommandType.CONSOLE);
        setShortHelp(Locale.get(Msg.CMD_USG_MANAGE_BACKUP).toString());
        setArguments("<DB>");

    }

    @Override
    public void onCommand(ISender sender, String commandLabel, String[] args) {
        try {

            if (!Condition.isTrue(args.length >= 1, Locale.get(Msg.CMD_FAIL_REQ_ARGS).parse(this.getArguments()), sender)) {
                return;
            }
            String dbName = args[0].toLowerCase();
            boolean isCorrectDB = "sqlite".equals(dbName) || "mysql".equals(dbName);
            if (!Condition.isTrue(isCorrectDB, Locale.get(Msg.MANAGE_FAIL_INCORRECT_DB) + dbName, sender)) {
                return;
            }

            final Database database = DBSystem.getActiveDatabaseByName(dbName);

            // If DB is null return
            if (!Condition.isTrue(Verify.notNull(database), Locale.get(Msg.MANAGE_FAIL_FAULTY_DB).toString(), sender)) {
                Log.error(dbName + " was null!");
                return;
            }
            Log.debug("Backup", "Start");
            runBackupTask(sender, args, database);
        } catch (DBInitException | NullPointerException e) {
            sender.sendMessage(Locale.get(Msg.MANAGE_FAIL_FAULTY_DB).toString());
        } finally {
            Log.logDebug("Backup");
        }
    }

    private void runBackupTask(ISender sender, String[] args, final Database database) {
        RunnableFactory.createNew(new AbsRunnable("BackupTask") {
            @Override
            public void run() {
                try {
                    sender.sendMessage(Locale.get(Msg.MANAGE_INFO_START).parse());
                    ManageUtils.backup(args[0], database);
                    sender.sendMessage(Locale.get(Msg.MANAGE_INFO_COPY_SUCCESS).toString());
                } catch (Exception e) {
                    Log.toLog(this.getClass(), e);
                    sender.sendMessage(Locale.get(Msg.MANAGE_INFO_FAIL).toString());
                } finally {
                    this.cancel();
                }
            }
        }).runTaskAsynchronously();
    }
}

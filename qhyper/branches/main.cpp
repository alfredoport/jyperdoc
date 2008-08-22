#include "qhyper.h"

#include <QtGui>
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    qhyper w;
    w.show();
    return a.exec();
}
